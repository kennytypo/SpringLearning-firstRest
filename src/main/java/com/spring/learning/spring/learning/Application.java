package com.spring.learning.spring.learning;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
//		SpringApplication.run(Application.class, args);
		RestApiDemoController radc = new RestApiDemoController();

	}
}

class Coffee {
	private final String id;
	private String name;
	public Coffee(String id, String name){
		this.id = id;
		this.name = name;
	}

	public Coffee(String name){
		this(UUID.randomUUID().toString(), name);
	}

	public String getId(){
		return id;
	}

	public String getName(){
		return name;
	}

	public void setName(String name){
		this.name = name;
	}
}
@RestController
@RequestMapping("/coffees")
class RestApiDemoController{
	private List<Coffee> coffees = new ArrayList<>();
	public RestApiDemoController(){
		coffees.addAll(List.of(new Coffee("Café Cereza"),
				new Coffee("Café Ganador"),
				new Coffee("Café Lareño"),
				new Coffee("Café Três Pontas")
		));
	}
	@GetMapping
	Iterable<Coffee> getCoffees(){
		return coffees;
	}
	/*
	* getCoffees description:
	* Возвращает итерируемую группу видов кофе, представленную переменной экземпляра coffees.
	*/
	@GetMapping("/{id}")
	Optional<Coffee> getCoffeeById(@PathVariable String id){
		for(Coffee c: coffees){
			if (c.getId().equals(id)){
				return Optional.of(c);
			}
		}
		return Optional.empty();
	}

	/*
	* getCoffeeById description:
	* Получает путь через id, благодаря аннотации @PathVariable
	* Проходя в цикле по списку видов кофе, возвращает заполненный Optional<Coffee> при обнаружении соответствия.
	* Если нужный объект не был найден, возврщает пустой Optional<Coffee>.
	*/

	@PostMapping
	Coffee postCoffee (@RequestBody Coffee coffee){
		coffees.add(coffee);
		return coffee;
	}
	/*
	* postCoffee description:
	* Получает информацию об указанном кофе в виде объекта Coffee.
	* Добавляет его в наш список coffees.
	* А затем возвращает его, запрашивающему сервису или приложению.
	*/


	@PutMapping("/{id}")
	ResponseEntity<Coffee> putCoffee(@PathVariable String id, @RequestBody Coffee coffee){
		int coffeeIndex = -1;

		for (Coffee c: coffees){
			if(c.getId().equals(id)){
				coffeeIndex = coffees.indexOf(c);
				coffees.set(coffeeIndex,coffee);
			}
		}
		return (coffeeIndex == -1) ?
				new ResponseEntity<>(postCoffee(coffee), HttpStatus.CREATED) :
				new ResponseEntity<>(coffee, HttpStatus.OK);
	}

	/*
	* putCoffee description:
	* По заданным параметрам, ищет кофе с индексом id в списке.
	* Если такой есть, то меняет переменную coffeeIndex на индекс искомого.
	* Изменяет значение по индексу coffeeIndex на coffee.
	* Если элемент не был найден в списке создает его и возвращает.
	*/

	@DeleteMapping("/{id}")
	void deleteCoffee(@PathVariable String id){
		coffees.removeIf(c -> c.getId().equals(id));
	}

	/*
	*  deleteCoffee description:
	*  Получает id элемента который нужно удалить.
	*  Удаляет элемент с помощью метода removeIf из интерфейса Collection.
	*  Метод removeIf получает Predicate, поэтому можно передать лямбда-выражение, возвращающее true для вида кофе, который нужно удалить.
	*/

}
