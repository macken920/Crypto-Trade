package pl.arbitrage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import pl.arbitrage.data.operations.DataDownloader;

@SpringBootApplication
public class SpringProjektTestowyApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringProjektTestowyApplication.class, args);
		
		DataDownloader datadownloader = new DataDownloader("bittrex","btc-ltc");
		DataDownloader datadownloader1 = new DataDownloader("binance","ltcbtc");
		DataDownloader datadownloader2 = new DataDownloader("hitbtc","ltcbtc");
		

		
		System.out.println(datadownloader.toString());
		System.out.println(datadownloader1.toString());
		System.out.println(datadownloader2.toString());
		
		
		
	}

}

