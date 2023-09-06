package Actions;

import java.util.Map;
import java.util.PriorityQueue;

public class BestDeals {
	/**
	 * method to find the top 5 deals based on the given map of products and their
	 * prices
	 * 
	 * @author Megha bhargava
	 * @param products
	 */

	public static void TopDeals(Map<String, Double> products) {
		// create a priority queue to store the products in ascending order of price
		PriorityQueue<Product> queue = new PriorityQueue<>();
		// add each product from the map to the priority queue as a Product object
		for (Map.Entry<String, Double> entry : products.entrySet()) {
			queue.add(new Product(entry.getKey(), entry.getValue()));
		}
		// print the names of the top 5 products in the priority queue, which will be
		// the lowest-priced products
		int length=products.size()>5?5:products.size();
		for (int i = 0; i <length; i++) {
			Product bestDeal = queue.poll();
			System.out.println(bestDeal.getName());
		}
	}

}

/**
 * a class to represent a product with its name and price
 * 
 * @author Megha Bhargava
 */
class Product implements Comparable<Product> {

	private String name;
	private double price;

	// constructor to create a new Product object with the given name and price
	public Product(String name, double price) {
		this.name = name;
		this.price = price;
	}

	// getter method for the name of the product
	public String getName() {
		return name;
	}

	// getter method for the price of the product
	public double getPrice() {
		return price;
	}

	// compare method to compare this product with another product based on their
	// prices
	@Override
	public int compareTo(Product other) {
		return Double.compare(this.price, other.price);
	}

}
