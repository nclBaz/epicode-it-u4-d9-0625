package riccardogulin;

import com.github.javafaker.Faker;
import riccardogulin.entities.User;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class Application {

	public static void main(String[] args) {
		Supplier<ArrayList<User>> randomUserSupplier = () -> {
			ArrayList<User> randomUsers = new ArrayList<>();
			Random random = new Random();
			Faker faker = new Faker(Locale.ITALIAN);
			for (int i = 0; i < 100; i++) {
				randomUsers.add(new User(faker.lordOfTheRings().character(),
						faker.name().lastName(),
						random.nextInt(1, 101),
						faker.lordOfTheRings().location()
				));
			}
			return randomUsers;
		};

		List<User> randomUsers = randomUserSupplier.get();

		System.out.println("********************************** COLLECTORS ************************************");

		// 1. Raggruppiamo gli utenti minorenni per città
		Map<String, List<User>> usersByCity = randomUsers.stream().filter(user -> user.getAge() > 18).collect(Collectors.groupingBy(user -> user.getCity()));
		usersByCity.forEach((city, usersList) -> System.out.println("Città: " + city + ", users: " + usersList));

		// 2. Raggruppiamo gli utenti per età
		Map<Integer, List<User>> usersByAge = randomUsers.stream().collect(Collectors.groupingBy(user -> user.getAge()));
		usersByAge.forEach((age, usersList) -> System.out.println("Età: " + age + ", users: " + usersList));

		// 3. Raggruppiamo gli utenti per nome
		Map<String, List<User>> usersByName = randomUsers.stream().collect(Collectors.groupingBy(user -> user.getName()));
		usersByName.forEach((name, usersList) -> System.out.println("Nome: " + name + ", users: " + usersList));

		// 4. Concateniamo tutti i nomi e cognomi in un'unica String "Aldo Baglio. Giovanni Storti. Giacomo Poretti. ....."
		String namesAndSurnames = randomUsers.stream().map(user -> user.getName() + " " + user.getSurname()).collect(Collectors.joining(" -- "));
		String namesAndSurnames2 = randomUsers.stream().map(user -> user.getName() + " " + user.getSurname())
				.reduce("", (partialStr, nameAndSurname) -> partialStr + " -- " + nameAndSurname);
		System.out.println(namesAndSurnames);
		System.out.println(namesAndSurnames2);

		// 5. Concateniamo tutte le età in unica stringa di età uniche
		Set<String> uniqueAgesSet = randomUsers.stream().map(user -> user.getAge() + "").collect(Collectors.toSet());
		String uniqueAges = uniqueAgesSet.stream().collect(Collectors.joining(". "));
		// N.B. Dallo stage precedente deve arrivare testo per fare il joining
		System.out.println(uniqueAgesSet);

	}
}
