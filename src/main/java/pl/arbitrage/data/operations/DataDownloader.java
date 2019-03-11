package pl.arbitrage.data.operations;

import java.io.IOException;
import java.net.URL;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class DataDownloader {
	
	private String exchange;
	private String ticker;
	private double bid;
	private double ask;
	private double sizeBid;
	private double sizeAsk;

	public DataDownloader(){}
	
	public DataDownloader (String exchange,String ticker) {
		this.setExchange(exchange.toLowerCase());
		this.setTicker(ticker);
		
		ObjectMapper mapper = new ObjectMapper();
		switch(exchange) {
			case "hitbtc": //https://api.hitbtc.com/api/2/public/orderbook/smartbtc
			    try {
			        JsonNode orderBook = mapper.readValue(new URL("https://api.hitbtc.com/api/2/public/orderbook/"+ticker), JsonNode.class);
			            
			        ask = orderBook.get("ask").get(0).get("price").asDouble();
			        bid = orderBook.get("bid").get(0).get("price").asDouble();
			        sizeBid = orderBook.get("bid").get(0).get("size").asDouble();
			        sizeAsk = orderBook.get("ask").get(0).get("size").asDouble();

			    } catch (IOException e) {
			            e.printStackTrace();
			        }
				break;
			case "binance"://https://api.binance.com/api/v3/ticker/bookTicker?symbol=LTCBTC
		        try {
		        	JsonNode orderBook = mapper.readValue(new URL("https://api.binance.com/api/v3/ticker/bookTicker?symbol="+ticker.toUpperCase()), JsonNode.class);
		            
		            ask = orderBook.get("askPrice").asDouble();
		            bid = orderBook.get("bidPrice").asDouble();
		            sizeBid = orderBook.get("bidQty").asDouble();
		            sizeAsk = orderBook.get("askQty").asDouble();

		        } catch (IOException e) {
		            e.printStackTrace();
		        }
				break;
			case "bittrex"://https://api.bittrex.com/api/v1.1/public/getorderbook?market=BTC-LTC&type=both
				try {
		        	JsonNode orderBook = mapper.readValue(new URL("https://api.bittrex.com/api/v1.1/public/getorderbook?market="+ticker+"&type=both"), JsonNode.class);
		            
		            ask = orderBook.get("result").get("sell").get(0).get("Rate").asDouble();
		            bid = orderBook.get("result").get("buy").get(0).get("Rate").asDouble();
		            sizeBid = orderBook.get("result").get("buy").get(0).get("Quantity").asDouble();
		            sizeAsk = orderBook.get("result").get("sell").get(0).get("Quantity").asDouble();

		        } catch (IOException e) {
		            e.printStackTrace();
		        }
				break;
			default: 
                System.out.println("no match");
                break;		
		}
		
	}
	
	
	@Override
	public String toString() {
		return "ask: " + ask + " bid: " + bid + " iloscbid: " + sizeBid + " ilosc ask: " + sizeAsk;
		
	}
	public void wyswietl() {
		System.out.println(ask + " " + bid);
	}

	public double getBid() {
		return bid;
	}


	public double getAsk() {
		return ask;
	}


	public double getSizeBid() {
		return sizeBid;
	}


	public double getSizeAsk() {
		return sizeAsk;
	}

	public String getExchange() {
		return exchange;
	}

	public void setExchange(String exchange) {
		this.exchange = exchange;
	}

	public String getTicker() {
		return ticker;
	}

	public void setTicker(String ticker) {
		this.ticker = ticker;
	}


}


// giełda - case
// waluta - url change
// bid ask sizebid sizeask