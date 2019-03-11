package pl.arbitrage.data.operations;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class LiquidityProvider {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	private String timeStamp;
	private String pair;
	private double price;

	
	
	public LiquidityProvider() {};
	public LiquidityProvider(String pair) {//ltcbtc:btc-ltc,ltctc,ltcbtc w przyszlosci rozne tickery teraz tylko 1
		this.pair = pair;
		
		switch(pair) {
		case "ltcbtc":
			
			DataDownloader datadownloader = new DataDownloader("bittrex","btc-ltc");
			DataDownloader datadownloader1 = new DataDownloader("binance","ltcbtc");
			DataDownloader datadownloader2 = new DataDownloader("hitbtc","ltcbtc");
			price = (datadownloader.getAsk() + datadownloader.getBid() + datadownloader1.getAsk() + datadownloader1.getBid() + datadownloader2.getAsk() + datadownloader2.getBid())/6;

			break;
		case "ethbtc":
			datadownloader = new DataDownloader("bittrex","btc-eth");
			datadownloader1 = new DataDownloader("bittrex","btc-eth");
			datadownloader2 = new DataDownloader("hitbtc","ethbtc");
			price = (datadownloader.getAsk() + datadownloader.getBid() + datadownloader1.getAsk() + datadownloader1.getBid() + datadownloader2.getAsk() + datadownloader2.getBid())/6;
			break;
		default: 
            System.out.println("no match");
            break;		
		}
		
		timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
			
	}



	public String getTimeStamp() {
		return timeStamp;
	}



	public Long getId() {
		return id;
	}



	public void setId(Long id) {
		this.id = id;
	}



	public String getPair() {
		return pair;
	}



	public void setPair(String pair) {
		this.pair = pair;
	}



	public double getPrice() {
		return price;
	}



	public void setPrice(double price) {
		this.price = price;
	}



	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}
	

}




