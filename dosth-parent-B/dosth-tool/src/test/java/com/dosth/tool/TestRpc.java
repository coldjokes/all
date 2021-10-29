package com.dosth.tool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.dosth.dto.Card;
import com.dosth.dto.ExtraCabinet;
import com.dosth.tool.entity.MatEquInfo;
import com.dosth.tool.entity.RestitutionType;
import com.dosth.tool.rpc.CabinetRpcService;
import com.dosth.tool.service.EquDetailStaService;
import com.dosth.tool.service.RestitutionTypeService;
import com.dosth.toolcabinet.dto.CartInfo;

public class TestRpc extends AppTest {

	private final String cabinetId = "297e5a8667960b070167969f51620013";

	@Autowired
	private CabinetRpcService cabinetRpcService;

	@Autowired
	private EquDetailStaService equDetailStaService;

	@Autowired
	private RestitutionTypeService restitutionTypeService;

	@Test
	public void a() {
		List<Card> cardList = this.cabinetRpcService.getCardList(this.cabinetId);
		cardList.forEach(card -> {
			System.out.println(card.getHost() + ":" + card.getPort());
			card.getLatticeList().forEach(lattice -> {
				System.out.println(lattice.getMatInfo().getName());
			});
		});
	}

//	@Test
//	public void b() {
//		List<MatEquType> typeList = this.matEquTypeService.findByMatEquType(MatEqu.MATERIAL.name());
//		typeList.forEach(type -> {
//			System.err.println(type.getTypeName());
//		});
//	}

	@Test
	public void c() {
		List<MatEquInfo> typeList = null;// this.cabinetRpcService.getMatTypeTree(this.cabinetId);
		typeList = this.equDetailStaService.getMatInfoListByCabinetId(this.cabinetId);
		typeList.forEach(type -> {
			System.err.println(type);
		});
	}

	@Test
	public void d() {
		RestitutionType type = this.restitutionTypeService.get("101");
		System.out.println(type);
	}

	@Test
	public void e() {
		List<CartInfo> cartList = new ArrayList<>();
		cartList.add(new CartInfo(1, "PACK", "MATTYPE", "数控机床", "2"));
		cartList.add(new CartInfo(1, "PACK", "MATTYPE", "数控机床", "12"));
		String accountId = "1";
		String shareSwitch = "TRUE";
		try {
			FutureTask<List<ExtraCabinet>> task = new FutureTask<>(new Callable<List<ExtraCabinet>>() {
				@Override
				public List<ExtraCabinet> call() throws Exception {
					return cabinetRpcService.sendCartToServer(cabinetId, cartList, shareSwitch, accountId);
				}
			});
			new Thread(task).start();
			List<ExtraCabinet> cabinetList = task.get();
			Map<ExtraCabinet, List<Card>> cardMap = new HashMap<ExtraCabinet, List<Card>>();
			for (ExtraCabinet cabinet : cabinetList) {
				cardMap.put(cabinet, cabinet.getCardList());
			}
			cardMap.forEach((cabinet, list) -> {
				System.out.println(cabinet.toString());
				list.forEach(card -> {
					System.out.println(card.getRowNo() + ">>" + card.getHost() + ":" + card.getPort());
				});
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}