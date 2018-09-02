package br.com.carlettisolucoes.coinvest.v2;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public class Okex extends Exchange {

//	private static String[] coins = {"btc_gto","btc_eos","btc_eth","btc_ltc","btc_trx","btc_etc","btc_true","btc_mith","btc_bch","btc_hsr","btc_xrp",
//			"btc_mco","btc_iota","btc_pra","btc_neo","btc_qtum","btc_elf","btc_hpb","btc_ont","btc_xlm","btc_pay","btc_swftc","btc_iost","btc_btm","btc_auto",
//			"btc_can","btc_theta","btc_int","btc_1st","btc_cvc","btc_kcash","btc_egt","btc_mana","btc_lba","btc_nas","btc_knc","btc_kan","btc_ors","btc_rct",
//			"btc_wtc","btc_gas","btc_dash","btc_zrx","btc_rfr","btc_ppt","btc_snt","btc_lrc","btc_omg","btc_zec","btc_btg","btc_act","btc_xmr","btc_icx",
//			"btc_dadi","btc_ren","btc_bkx","btc_mof","btc_itc","btc_aac","btc_gnt","btc_zip","btc_r","btc_abt","btc_ugc","btc_mtl","btc_cmt","btc_ctxc",
//			"btc_ace","btc_ins","btc_ssc","btc_mdt","btc_storj","btc_xem","btc_dpy","btc_show","btc_mth","btc_gtc","btc_tnb","btc_stc","btc_snc","btc_zil",
//			"btc_light","btc_ae","btc_trio","btc_vib","btc_insur","btc_qun","btc_bcd","btc_dent","btc_wrc","btc_bnt","btc_hmc","btc_ada","btc_sbtc","btc_zen",
//			"btc_lend","btc_yee","btc_rcn","btc_gnx","btc_link","btc_dgb","btc_nuls","btc_dna","btc_gsc","btc_spf","btc_dnt","btc_key","btc_sc","btc_mot",
//			"btc_soc","btc_bcn","btc_sub","btc_aidoc","btc_tct","btc_mvp","btc_topc","btc_poe","btc_tio","btc_waves","btc_hot","btc_pst","btc_of","btc_vee",
//			"btc_icn","btc_xas","btc_cvt","btc_fair","btc_bcx","btc_enj","btc_mag","btc_chat","btc_ngc","btc_nano","btc_dcr","btc_ast","btc_atl","btc_rnt",
//			"btc_yoyo","btc_rdn","btc_nxt","btc_ardr","btc_eng","btc_read","btc_req","btc_dat","btc_fun","btc_mda","btc_xuc","btc_salt","btc_utk","btc_ark",
//			"btc_ost","btc_dgd","btc_viu","btc_cbt","btc_lev","btc_mkr","btc_snm","btc_brd","btc_let","btc_evx","btc_amm","btc_san","btc_cag","btc_sngls",
//			"btc_ipc","btc_lsk","btc_zco","btc_ref","btc_ubtc","btc_oax","btc_edo","btc_la","btc_qvt","btc_bt2","btc_ukg","btc_avt"};
//	
//	private String pathMarket;
//	
//	@Override
//	public void recordMarket(OutputStream os) throws IOException {
//		os.write("[".getBytes());
//		boolean alreadyWrite = false;
//		for (String coin : coins) {
//			this.pathMarket = "/future_ticker.do?symbol="+coin+"&contract_type=this_week";
//			ByteArrayOutputStream baos = new ByteArrayOutputStream();
//			try {
//				super.recordMarket(baos);
//				byte[] bytes = baos.toByteArray();
//				if(bytes.length > 0) {
//					if(alreadyWrite) {
//						os.write(",".getBytes());
//					}
//					os.write(bytes);
//					alreadyWrite = true;
//				}
//			} catch (Exception e) {
//				e.printStackTrace();
//			} finally {
//				baos.close();
//			}
//		}
//		os.write("]".getBytes());
//		os.close();
//	}
	
	public String getApi() {
		return "https://www.okex.com/api/v1";
	}
	
	@Override
	String getPathMarket() {
		return "/tickers.do";
	}

	@Override
	String getName() {
		return "okex";
	}

	@Override
	List<String> getCoinsPairBTC() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	String getTokenSymbol() {
		return "symbol";
	}

	@Override
	String getTokenLastPrice() {
		return "last";
	}

	@Override
	String getTokenAskPrice() {
		return "sell";
	}

	@Override
	String getTokenBidPrice() {
		return "buy";
	}

	@Override
	String getTokenVolume() {
		return "vol";
	}
	
	@Override
	Object[] getPathToReachList() {
		return new Object[] {"tickers"};
	}

	@Override
	boolean readyToTrade() {
		return false;
	}
	
}