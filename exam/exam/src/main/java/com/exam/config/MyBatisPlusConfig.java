package com.exam.config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.JdbcType;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import com.baomidou.mybatisplus.MybatisConfiguration;
import com.baomidou.mybatisplus.entity.GlobalConfiguration;
import com.baomidou.mybatisplus.mapper.LogicSqlInjector;
import com.baomidou.mybatisplus.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.plugins.PerformanceInterceptor;
import com.baomidou.mybatisplus.spring.MybatisSqlSessionFactoryBean;
import com.exam.config.db.MultipleDataSource;
import com.exam.entity.enums.DataSourceEnum;
import com.google.common.collect.Lists;

/**
 * mybatis plus相关配置
 * 
 * @author Yifeng Wang  
 */
@Configuration
@MapperScan("com.exam.mapper*")  //TODO 路径
public class MyBatisPlusConfig {

	@Autowired
	private ProfileConfig profileConfig;
	
    @Bean("sqlSessionFactory")
    public SqlSessionFactory sqlSessionFactory() throws Exception {
        MybatisSqlSessionFactoryBean sqlSessionFactory = new MybatisSqlSessionFactoryBean();
        sqlSessionFactory.setDataSource(multipleDataSource(defaultDB(),outerDB()));
        //配置类型别名
        sqlSessionFactory.setTypeAliasesPackage("com.exam.entity.model"); //TODO 路径
        
        // 配置mapper的扫描，找到所有的mapper.xml映射文件
        Resource[] resources = new PathMatchingResourcePatternResolver() .getResources("classpath:/com/exam/mapper/*.xml"); //TODO 路径
        sqlSessionFactory.setMapperLocations(resources);
        
        MybatisConfiguration configuration = new MybatisConfiguration();
        //configuration.setDefaultScriptingLanguage(MybatisXMLLanguageDriver.class);
        configuration.setJdbcTypeForNull(JdbcType.NULL);
        configuration.setMapUnderscoreToCamelCase(true);
        configuration.setCacheEnabled(false);
        sqlSessionFactory.setConfiguration(configuration);

        String profile = profileConfig.getActiveProfile();
        
        List<Interceptor> interceptorList = Lists.newArrayList();
        interceptorList.add(paginationInterceptor()); //添加分页功能
        if(ProfileConfig.PROFILE_DEV.contentEquals(profile)) { //测试环境dev添加此插件
        	interceptorList.add(performanceInterceptor()); //SQL执行效率插件
        }
        sqlSessionFactory.setPlugins(interceptorList.toArray(new Interceptor[interceptorList.size()]));
        
        sqlSessionFactory.setGlobalConfig(globalConfiguration());
        return sqlSessionFactory.getObject();
    }

    @Bean(name = "default")
    @ConfigurationProperties(prefix = "spring.datasource.default" )
    public DataSource defaultDB() {
        return DruidDataSourceBuilder.create().build();
    }

    @Bean(name = "outer")
    @ConfigurationProperties(prefix = "spring.datasource.outer" )
    public DataSource outerDB() {
        return DruidDataSourceBuilder.create().build();
    }

    /**
          * 动态数据源配置
     */
    @Bean
    @Primary
    public DataSource multipleDataSource(@Qualifier("default") DataSource db1, @Qualifier("outer") DataSource db2) {
        MultipleDataSource multipleDataSource = new MultipleDataSource();
        
        //添加数据源
        Map< Object, Object> targetDataSources = new HashMap<>();
        targetDataSources.put(DataSourceEnum.DEFAULT.getValue(), db1);
        targetDataSources.put(DataSourceEnum.OUTER.getValue(), db2);
        multipleDataSource.setTargetDataSources(targetDataSources);
        
        //设置默认数据源
        multipleDataSource.setDefaultTargetDataSource(db1);
        return multipleDataSource;
    }

    /**
          * 全局策略配置
     */
    @Bean
    public GlobalConfiguration globalConfiguration() {
        GlobalConfiguration conf = new GlobalConfiguration(new LogicSqlInjector());
        //conf.setLogicDeleteValue("-1");
        //conf.setLogicNotDeleteValue("1");
        //conf.setMetaObjectHandler(new MyMetaObjectHandler());
        conf.setRefresh(true);
        conf.setIdType(0); //全局主键自增策略，0表示auto
        conf.setDbColumnUnderline(true); //驼峰命名。2.3版本后，默认值就是true
        conf.setTablePrefix("exam_"); //全局表前缀配置  //TODO 路径
        return conf;
    }
    
	/**
          * 分页插件，自动识别数据库类型
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
        paginationInterceptor.setLocalPage(true); // 开启 PageHelper 的支持
        return paginationInterceptor;
    }

    /**
     * SQL执行效率插件
     */
    @Bean
    public PerformanceInterceptor performanceInterceptor() {
        PerformanceInterceptor performanceInterceptor = new PerformanceInterceptor();
        // SQL执行性能分析，开发环境使用，线上不推荐。 maxTime指的是 sql 最大执行时长
        performanceInterceptor.setMaxTime(5000);
        // SQL是否格式化 默认false
        performanceInterceptor.setFormat(true);
        return performanceInterceptor;
    }
}
