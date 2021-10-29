package com.dosth.instruction.board.enums;

/**
 * @description 步骤
 * @author admin
 *
 */
public enum Step {
	// 读取状态
	READSTATUS,
	// 等待返回结果
	WAITTING,
	// 初始化
	INIT,
	// 伺服器复位
	SERVORESET,
	// 启动
	START,
	// 料斗
	HOPE,
	// 同层
	LEVEL,
	// 同一层
	SAMELEVEL,
	// 去门口
	TODOOR,
	// 开灯
	ONLIGHT,
	// 开门
	OPENDOOR,
	// 关门
	CLOSEDOOR,
	// 关灯
	OFFLIGHT,
	// 结束
	FINISH;
}