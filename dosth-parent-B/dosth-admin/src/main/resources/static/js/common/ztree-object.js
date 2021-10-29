/**
 * ztree插件的封装
 */
(function() {

	var $ZTree = function(id, url) {
		this.id = id;
		this.url = url;
		this.onClick = null;
		this.settings = null;
		this.ondblclick=null;
	};

	$ZTree.prototype = {
		/**
		 * 初始化ztree的设置
		 */
		initSetting : function() {
			var settings = {
				view : {
					dblClickExpand : true,
					selectedMulti : false
				},
				data : {simpleData : {enable : true}},
				callback : {
					onClick : this.onClick,
					onDblClick:this.ondblclick
				}
			};
			return settings;
		},

		/**
		 * 手动设置ztree的设置
		 */
		setSettings : function(val) {
			this.settings = val;
		},

		/**
		 * 初始化ztree
		 */
		init : function() {
			var zNodeSeting = null;
			if(this.settings != null){
				zNodeSeting = this.settings;
			}else{
				zNodeSeting = this.initSetting();
			}
			var zNodes = this.loadNodes();
			$.fn.zTree.init($("#" + this.id), zNodeSeting, zNodes);
		},

		/**
		 * 绑定onclick事件
		 */
		bindOnClick : function(func) {
			this.onClick = func;
		},
		/**
		 * 绑定双击事件
		 */
		bindOnDblClick : function(func) {
			this.ondblclick=func;
		},


		/**
		 * 加载节点
		 */
		loadNodes : function() {
			var zNodes = null;
			var ajax = new $ax(Feng.ctxPath + this.url, function(data) {
				zNodes = data;
			}, function(data) {
				Feng.error("加载ztree信息失败!");
			});
			ajax.start();
			return zNodes;
		},

		/**
		 * 获取选中的值
		 */
		getSelectedVal : function(){
			var zTree = $.fn.zTree.getZTreeObj(this.id);
			var nodes = zTree.getSelectedNodes();
			return nodes[0].name;
		},
		/**
		 * 获取指定参数获取节点选择
		 */
		setNodeSelectByParam : function(param, val, checked) {
			var zTree = $.fn.zTree.getZTreeObj(this.id);
			var node = zTree.getNodeByParam(param, val);  
			zTree.selectNode(node, checked != undefined && checked == 1);;
		},
		/**
		 * 获取选中节点
		 */
		getSelectedNode : function() {
			var zTree = $.fn.zTree.getZTreeObj(this.id);
			var node = zTree.getSelectedNodes();  
			return node;
		},
		/**
		 * 获取指定参数获取节点选中
		 */
		setNodeCheckByParam : function(param, val, checked) {
			var zTree = $.fn.zTree.getZTreeObj(this.id);
			var node = zTree.getNodeByParam(param, val);  
			zTree.checkNode(node, checked != undefined && checked == 1);
		},
		/**
		 * 清除所有选中
		 */
		clearAllCheck : function(){
			var zTree = $.fn.zTree.getZTreeObj(this.id);
			var nodes = zTree.getNodes();
			for (var i = 0; i < nodes.length; i++) {
				zTree.selectNode(nodes[i], false);
				zTree.checkNode(nodes[i], false);
			}
		}
	};

	window.$ZTree = $ZTree;

}());