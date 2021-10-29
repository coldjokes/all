package com.dosth.tool.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;

import com.dosth.common.db.repository.BaseRepository;
import com.dosth.tool.entity.MatReturnBack;

/**
 * 
 * @description 物料归还持久化层
 * @author guozhidong
 *
 */
public interface MatReturnBackRepository extends BaseRepository<MatReturnBack, String> {

	/**
	 * 获取归还详情
	 * 
	 * @param code
	 * @return
	 */
	@Query("from MatReturnBack y where y.matUseBillId = :code")
	public MatReturnBack findById(String code);
	
	/**
	 * 获取归还详情
	 * 
	 * @param code
	 * @return
	 */
	@Query("from MatReturnBack y where y.returnBackNo = :returnBackNo")
	public List<MatReturnBack> findByReturnNo(int returnBackNo);

	/**
	 * 根据绑定条形码或归还码获取归还详情
	 * 
	 * @param code
	 * @return
	 */
	@Query("from MatReturnBack y where y.barCode = :code or y.returnBackNo = :returnBackNo and y.isReturnBack = 'ISBACK'")
	public List<MatReturnBack> findBybarCode(String code, int returnBackNo);
	
	/**
	 * 条形码重复使用查询
	 * 
	 * @param code
	 * @return
	 */
	@Query("from MatReturnBack y where y.barCode = :code")
	public MatReturnBack checkbarCode(String code);
	
	/**
	 * 根据归还ID查询
	 * 
	 * @param code
	 * @return
	 */
	@Query("from MatReturnBack y where y.id = :code")
	public MatReturnBack findByBackId(String code);
	
	/**
	 * @description 获取需要同步的归还记录
	 * @param cabinetName
	 * @param endTime 
	 * @param beginTime
	 * @return
	 */
	@Query("from MatReturnBack b where b.opDate >:beginTime and b.opDate <=:endTime and b.isReturnBack = 'ISBACK' and b.matUseBill.equDetailSta.equDetail.equSetting.wareHouseAlias =:cabinetName")
	public List<MatReturnBack> getSyncBackInfo(String cabinetName, Date beginTime, Date endTime);
	
	/**
	 * 获取最大流水号
	 * 
	 * @param code
	 * @return
	 */
	@Query("select returnBackNo from MatReturnBack order by returnBackNo desc")
	public List<Integer> getMaxReturnNo();
}