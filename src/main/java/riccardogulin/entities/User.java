package riccardogulin.entities;

public class User {
	private String name;
	private String surname;
	private int age;
	private String city;

	public User(String name, String surname, int age, String city) {
		this.name = name;
		this.surname = surname;
		this.age = age;
		this.city = city;
	}

	public String getName() {
		return name;
	}

	public String getSurname() {
		return surname;
	}

	public int getAge() {
		return age;
	}

	public String getCity() {
		return city;
	}

	@Override
	public String toString() {
		return "User{" +
				"name='" + name + '\'' +
				", surname='" + surname + '\'' +
				", age=" + age +
				", city='" + city + '\'' +
				'}';
	}
}
