package br.com.carlettisolucoes.coinvest.quotation;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.carlettisolucoes.coinvest.cexio.CexioApi;
import br.com.carlettisolucoes.coinvest.cexio.LastPrice;
import br.com.carlettisolucoes.coinvest.coin.Coin;
import br.com.carlettisolucoes.coinvest.coin.CoinRepository;
import br.com.carlettisolucoes.coinvest.wallet.Wallet;

@Service
public class QuotationService {

	private static final Logger log = LoggerFactory.getLogger(QuotationService.class);
	
	@Autowired
	private QuotationRepository quotationRepo;
	
	@Autowired
	private CoinRepository coinRepo;	
	
	@Autowired
	private QuotationRepositoryImpl quotationRepoImpl;	
	
	@Autowired
	private CexioApi cexioApi;	
	
	public void updater(Iterable<Wallet> wallets) {
		for (Wallet wallet : wallets) {
//			log.info("Wallet got: {}", wallet);
			Coin coin = wallet.getCoin();
			List<Quotation> quotations = quotationRepoImpl.getLatest(1, coin.getId());
			LastPrice lastPrice = cexioApi.getLastPrice(coin.getPath());
			if(lastPrice == null) {
				continue;
			}
			Quotation quotation = new Quotation();
			quotation.setDatetime(new Date());
			quotation.setValue(new BigDecimal(lastPrice.getLprice()));
			quotation.setCoin(coin);
			if(quotations != null && !quotations.isEmpty()) {
				quotation.setPreviousQuotation(quotations.get(0));
			}
			quotationRepo.save(quotation);
//			log.info("Save quotation: {}", quotation);
			BigDecimal[][] history = quotationRepoImpl.getHistory(20, 1, coin.getId());
			if(history.length > 0) {
				BigDecimal maxUp = getMaxUpPercent(history[0]);
				BigDecimal maxDown = getMaxDownPercent(history[0]);
//				log.info("maxUp: {}", maxUp);
//				log.info("maxDown: {}", maxDown);
				coin.setMaxUp(maxUp);
				coin.setMaxDown(maxDown);
				if(coin.getMaxUsd() == null || coin.getMaxUsd().compareTo(quotation.getValue()) == -1) {
					coin.setMaxUsd(quotation.getValue());
				}
				if(coin.getMinUsd() == null || coin.getMinUsd().compareTo(quotation.getValue()) == 1) {
					coin.setMinUsd(quotation.getValue());
				}
				if(coin.getMaxUpHistory() == null || coin.getMaxUpHistory().compareTo(maxUp) == -1) {
					coin.setMaxUpHistory(maxUp);
				}
				if(coin.getMaxDownHistory() == null || coin.getMaxDownHistory().compareTo(maxDown) == 1) {
					coin.setMaxDownHistory(maxDown);
				}
				coinRepo.save(coin);
			}
		}
	}
	
	private BigDecimal getMaxUpPercent(BigDecimal[] numbers) {
		BigDecimal hundred = new BigDecimal(100);
		BigDecimal higher = BigDecimal.ZERO;
		BigDecimal lower = BigDecimal.ZERO;
		BigDecimal actualHigher = BigDecimal.ZERO;
		BigDecimal actualLower = BigDecimal.ZERO;		
		for (int i = 1; i < numbers.length; i++) {
			if(numbers[i].compareTo(numbers[i - 1]) >= 0) { //previous bigger than actual
				if(actualLower.equals(BigDecimal.ZERO)) {
					actualLower = numbers[i - 1];
				}
				actualHigher = numbers[i];
			} else {
				boolean change = higher.equals(BigDecimal.ZERO);
				if(!change && !lower.equals(BigDecimal.ZERO) && !actualLower.equals(BigDecimal.ZERO)) {
					BigDecimal actualPercent = actualHigher.multiply(hundred).divide(actualLower, 6, RoundingMode.HALF_UP).subtract(hundred);
					BigDecimal percent = higher.multiply(hundred).divide(lower, 6, RoundingMode.HALF_UP).subtract(hundred);
					if(actualPercent.compareTo(percent) == 1) {
						change = true;
					}
				}
				if(change) {
					higher = actualHigher;
					lower = actualLower;
				}
				actualHigher = BigDecimal.ZERO;
				actualLower = BigDecimal.ZERO;
			}
		}
		if(lower.signum() == 0) {
			return BigDecimal.ZERO;
		}		
		BigDecimal result = higher.multiply(hundred).divide(lower, 6, RoundingMode.HALF_UP).subtract(hundred);
		return result;
	}
	
	private BigDecimal getMaxDownPercent(BigDecimal[] numbers) {
		BigDecimal hundred = new BigDecimal(100);
		BigDecimal higher = BigDecimal.ZERO;
		BigDecimal lower = BigDecimal.ZERO;
		BigDecimal actualHigher = BigDecimal.ZERO;
		BigDecimal actualLower = BigDecimal.ZERO;		
		for (int i = 1; i < numbers.length; i++) {
			if(numbers[i].compareTo(numbers[i - 1]) <= 0) {
				if(actualHigher.equals(BigDecimal.ZERO)) {
					actualHigher = numbers[i - 1];
				}
				actualLower = numbers[i];
			} else {
				boolean change = lower.equals(BigDecimal.ZERO);
				if(!change && !higher.equals(BigDecimal.ZERO) && !actualHigher.equals(BigDecimal.ZERO)) {
					BigDecimal actualPercent = actualLower.multiply(hundred).divide(actualHigher, 6, RoundingMode.HALF_UP).subtract(hundred);
					BigDecimal percent = lower.multiply(hundred).divide(higher, 6, RoundingMode.HALF_UP).subtract(hundred);
					if(percent.compareTo(actualPercent) == 1) {
						change = true;
					}
				}
				if(change) {
					higher = actualHigher;
					lower = actualLower;
				}
				actualHigher = BigDecimal.ZERO;
				actualLower = BigDecimal.ZERO;
			}
		}
		if(higher.signum() == 0) {
			return BigDecimal.ZERO;
		}
		BigDecimal result = lower.multiply(hundred).divide(higher, 6, RoundingMode.HALF_UP).subtract(hundred);
		return result;
	}	
		
}
