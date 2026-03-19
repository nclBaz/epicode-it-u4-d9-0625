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

		// 6. Somma delle età degli utenti
		int total = randomUsers.stream().collect(Collectors.summingInt(user -> user.getAge()));
		System.out.println(total);

		// 7. Media delle età degli utenti
		double average = randomUsers.stream().collect(Collectors.averagingInt(user -> user.getAge()));
		System.out.println(average);

		// 8. Raggruppiamo gli utenti per città e calcoliamo l'età media per ogni città
		Map<String, Double> averageAgePerCity = randomUsers.stream().collect(
				Collectors.groupingBy(
						user -> user.getCity(),
						Collectors.averagingInt(user -> user.getAge())));

		averageAgePerCity.forEach((city, averageAge) -> System.out.println("City: " + city + ", average: " + averageAge));

		// 8. Raggruppiamo gli utenti per città e calcoliamo per ogni città una serie di statistiche tipo media, somma, età minima, età massima
		Map<String, IntSummaryStatistics> statsPerCity = randomUsers.stream().collect(
				Collectors.groupingBy(
						user -> user.getCity(),
						Collectors.summarizingInt(user -> user.getAge())
				));

		statsPerCity.forEach((city, statistics) -> System.out.println("City: " + city + ", statistics: " + statistics));

		System.out.println("------------------------------------------------ COMPARATORS -------------------------------------------------");

		// 1. Ordiniamo gli utenti per età crescente
		List<User> sortedByAge = randomUsers.stream().sorted(Comparator.comparing(user -> user.getAge())).toList();
		// List<User> sortedByAge = randomUsers.stream().sorted(Comparator.comparing(User::getAge)).toList();
		sortedByAge.forEach(user -> System.out.println(user));
		sortedByAge.forEach(System.out::println); // ALTERNATIVA PIU'COMPATTA ALLA RIGA SOPRA

		// 2. Ordiniamo gli utenti per età decrescente
		List<User> sortedByAgeReverse = randomUsers.stream().sorted(Comparator.comparing(User::getAge).reversed()).toList();
		// Se uso reversed non posso usare la sintassi con le lambda ma devo usare quella con ::
		sortedByAgeReverse.forEach(System.out::println);

		// 3. Ordiniamo per cognome
		List<User> sortedBySurname = randomUsers.stream().sorted(Comparator.comparing(user -> user.getSurname())).toList();
		sortedBySurname.forEach(System.out::println);

		// 4. Ordiniamo per età e raggruppiamo per città
		Map<String, List<User>> usersGroupedByCitySortedBy = randomUsers.stream().sorted(Comparator.comparing(User::getAge)).collect(Collectors.groupingBy(User::getCity));
		usersGroupedByCitySortedBy.forEach((city, users) -> System.out.println("Città: " + city + ", " + users));

		System.out.println("------------------------------------- LIMIT ------------------------------------------");

		// 1. Otteniamo la lista dei 10 user più vecchi
		List<User> top10Users = randomUsers.stream().sorted(Comparator.comparing(User::getAge).reversed()).limit(10).toList();
		top10Users.forEach(System.out::println);

		System.out.println("------------------------------------- SKIP ------------------------------------------");

		List<User> other10Users = randomUsers.stream().sorted(Comparator.comparing(User::getAge).reversed()).skip(10).limit(10).toList();
		other10Users.forEach(System.out::println);

		System.out.println("------------------------------------- MAP TO ------------------------------------------");

		// 1. Somma delle età degli user con metodo map + reduce
		int totalAges = randomUsers.stream().map(user -> user.getAge()).reduce(0, (acc, currentAge) -> acc + currentAge);
		System.out.println("Somma tramite reduce: " + totalAges);

		// 2. Somma delle età tramite collect & summingInt
		int totalAges2 = randomUsers.stream().collect(Collectors.summingInt(user -> user.getAge()));
		System.out.println("Somma tramite summingInt: " + totalAges2);

		// 3. Somma delle età tramite mapToInt
		int totalAges3 = randomUsers.stream().mapToInt(User::getAge).sum();
		System.out.println("Somma tramite mapToInt: " + totalAges3);

		// 4. Media delle età tramite mapToInt
		OptionalDouble average2 = randomUsers.stream().mapToInt(User::getAge).average();
		if (average2.isPresent()) System.out.println("La media è: " + average2.getAsDouble());
		else System.out.println("Non posso calcolare la media perché la lista è vuota");

		// 5. Calcolo dell'età massima tramite mapToInt
		OptionalInt maxAge = randomUsers.stream().mapToInt(User::getAge).max();
		if (maxAge.isPresent()) System.out.println("L'età massima è: " + maxAge.getAsInt());
		else System.out.println("Non è stato possibile determinare l'età massima perché lista vuota");

		// 6. Calcolo delle statistiche sull'età tramite mapToInt
		IntSummaryStatistics stats = randomUsers.stream().mapToInt(User::getAge).summaryStatistics();
		System.out.println(stats);

	}
}
