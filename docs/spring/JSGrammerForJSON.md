# JSON 데이터를 다루기 위한 기본적인 JS 사용법
### [1] : 데이터
```js
const person = [
	{
		"name":"홍길동",
		"age":20,
		"nationality":"대한민국"
	},
	{
		"name":"이순신",
		"age":30,
		"nationality":"미국"
	},
	{
		"name":"강감찬",
		"age":40,
		"nationality":"영국"
	},
	{
		"name":"을지문덕",
		"age":50,
		"nationality":"프랑스"
	},
]

console.log("person : " + typeof person); //object
console.log("person[0] : " + typeof person[0]); //object
```
-----------------------------------------------------------------------------------------

### [2] : 출력
```js
console.log("--------------------------------------------");
console.log(person[0].name + " "+ person[0].age + " " + person[0].nationality);
console.log(person[3].name + " "+ person[3].age + " " + person[3].nationality);
```
-----------------------------------------------------------------------------------------

### [3] : 반복
```js
console.log("-------------------------------------------- 전개 연산자");
const str1 = "korea";
console.log(...str1);			
console.log([...str1]);			//__proto__ --> Array
console.log({...str1});			//__proto__ --> Object

console.log(...person);
console.log([...person]);
console.log([...person].length);  //4
console.log([...person][0].name); //홍길동
console.log([...person][3].name); //을지문덕
console.log([...person][3].age);  //50

console.log({...person});
console.log({...person}[1].name); //이순신
```
-----------------------------------------------------------------------------------------

### [4] : 반복 가능한 객체 --> for .. of, ...(전개 연산자)
```js
console.log("-------------------------------------------- for .. of");
for(let ele of person)	{ 
	//person --> iterable 즉, 반복 가능한 배열 같은 객체가 오면 된다. 기본적으로 객체는 반복이 가능하지 않다.
	console.log(ele)
}
```
