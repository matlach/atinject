package org.atinject.core.json;

import java.util.ArrayList;
import java.util.List;

import org.atinject.integration.WeldRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectMapper.DefaultTyping;

@RunWith(WeldRunner.class)
public class DTOObjectMapperTest 
{

    public class Zoo
    {
        public Animal animal;
    }

    public static class Animal
    {
        public String name;

        protected Animal()
        {
        }
    }

    public static class Dog extends Animal
    {
        public double barkVolume;

        public Dog()
        {
        }
    }

    public static class Cat extends Animal
    {
        boolean likesCream;
        public int lives;

        public Cat()
        {
        }
    }
    
    private ObjectMapper getDefaultObjectMapper(){
        return new ObjectMapper();
    }
    
    private Zoo getEmptyZoo(){
        Zoo zoo = new Zoo();
        return zoo;
    }
    
    private Zoo getDogInZoo(){
        Zoo zoo = new Zoo();
        Dog dog = new Dog();
        dog.name = "fido";
        dog.barkVolume = 90;
        zoo.animal = dog;
        return zoo;
    }
    
    @Test
    public void testDefaultObjectMapperSerializeEmptyZoo() throws Exception{
        System.out.println("testDefaultObjectMapperSerializeEmptyZoo");
        ObjectMapper objectMapper = getDefaultObjectMapper();
        String json = objectMapper.writeValueAsString(getEmptyZoo());
        System.out.println(json);
        System.out.println();
    }
    
    @Test
    public void testDefaultObjectMapperSerializeDogInZoo() throws Exception{
        System.out.println("testDefaultObjectMapperSerializeDogInZoo");
        ObjectMapper objectMapper = getDefaultObjectMapper();
        String json = objectMapper.writeValueAsString(getDogInZoo());
        System.out.println(json);
        System.out.println();
    }
    
    private ObjectMapper getObjectMapperWithDefaultTyping(){
        return new ObjectMapper().enableDefaultTyping();
    }
    
    @Test
    public void testObjectMapperWithDefaultTypingSerializeEmptyZoo() throws Exception{
        System.out.println("testObjectMapperWithDefaultTypingSerializeEmptyZoo");
        ObjectMapper objectMapper = getObjectMapperWithDefaultTyping();
        String json = objectMapper.writeValueAsString(getEmptyZoo());
        System.out.println(json);
        System.out.println();
    }
    
    @Test
    public void testObjectMapperWithDefaultTypingSerializeDogInZoo() throws Exception{
        System.out.println("testObjectMapperWithDefaultTypingSerializeDogInZoo");
        ObjectMapper objectMapper = getObjectMapperWithDefaultTyping();
        String json = objectMapper.writeValueAsString(getDogInZoo());
        System.out.println(json);
        System.out.println();
    }
    
    private ObjectMapper getObjectMapperWithNonFinalDefaultTyping(){
        return new ObjectMapper().enableDefaultTyping(DefaultTyping.NON_FINAL);
    }
    
    @Test
    public void testObjectMapperWithNonFinalDefaultTypingSerializeEmptyZoo() throws Exception{
        System.out.println("testObjectMapperWithNonFinalDefaultTypingSerializeEmptyZoo");
        ObjectMapper objectMapper = getObjectMapperWithNonFinalDefaultTyping();
        String json = objectMapper.writeValueAsString(getEmptyZoo());
        System.out.println(json);
        System.out.println();
    }
    
    @Test
    public void testObjectMapperWithNonFinalDefaultTypingSerializeDogInZoo() throws Exception{
        System.out.println("testObjectMapperWithNonFinalDefaultTypingSerializeDogInZoo");
        ObjectMapper objectMapper = getObjectMapperWithNonFinalDefaultTyping();
        String json = objectMapper.writeValueAsString(getDogInZoo());
        System.out.println(json);
        System.out.println();
    }
    
    @JsonTypeInfo(use=JsonTypeInfo.Id.CLASS, include=JsonTypeInfo.As.PROPERTY, property="@class")
    public class AnnotatedZoo
    {
        public AnnotatedAnimal animal;
    }

    @JsonTypeInfo(use=JsonTypeInfo.Id.CLASS, include=JsonTypeInfo.As.PROPERTY, property="@class")
    public static class AnnotatedAnimal
    {
        public String name;

        protected AnnotatedAnimal()
        {
        }
    }

    @JsonTypeInfo(use=JsonTypeInfo.Id.CLASS, include=JsonTypeInfo.As.PROPERTY, property="@class")
    public static class AnnotatedDog extends AnnotatedAnimal
    {
        public double barkVolume;

        public AnnotatedDog()
        {
        }
    }

    @JsonTypeInfo(use=JsonTypeInfo.Id.CLASS, include=JsonTypeInfo.As.PROPERTY, property="@class")
    public static class AnnotatedCat extends AnnotatedAnimal
    {
        boolean likesCream;
        public int lives;

        public AnnotatedCat()
        {
        }
    }
    
    private AnnotatedZoo getAnnotatedEmptyZoo(){
        AnnotatedZoo zoo = new AnnotatedZoo();
        return zoo;
    }
    
    private AnnotatedZoo getAnnotatedDogInZoo(){
        AnnotatedZoo zoo = new AnnotatedZoo();
        AnnotatedDog dog = new AnnotatedDog();
        dog.name = "fido";
        dog.barkVolume = 90;
        zoo.animal = dog;
        return zoo;
    }
    
    @Test
    public void testDefaultObjectMapperSerializeAnnotatedEmptyZoo() throws Exception{
        System.out.println("testDefaultObjectMapperSerializeAnnotatedEmptyZoo");
        ObjectMapper objectMapper = getDefaultObjectMapper();
        String json = objectMapper.writeValueAsString(getAnnotatedEmptyZoo());
        System.out.println(json);
        System.out.println();
    }
    
    @Test
    public void testDefaultObjectMapperSerializeAnnotatedDogInZoo() throws Exception{
        System.out.println("testDefaultObjectMapperSerializeAnnotatedDogInZoo");
        ObjectMapper objectMapper = getDefaultObjectMapper();
        String json = objectMapper.writeValueAsString(getAnnotatedDogInZoo());
        System.out.println(json);
        System.out.println();
    }
    
    @Test
    public void testObjectMapperWithDefaultTypingSerializeAnnotatedEmptyZoo() throws Exception{
        System.out.println("testObjectMapperWithDefaultTypingSerializeAnnotatedEmptyZoo");
        ObjectMapper objectMapper = getObjectMapperWithDefaultTyping();
        String json = objectMapper.writeValueAsString(getAnnotatedEmptyZoo());
        System.out.println(json);
        System.out.println();
    }
    
    @Test
    public void testObjectMapperWithDefaultTypingSerializeAnnotatedDogInZoo() throws Exception{
        System.out.println("testObjectMapperWithDefaultTypingSerializeAnnotatedDogInZoo");
        ObjectMapper objectMapper = getObjectMapperWithDefaultTyping();
        String json = objectMapper.writeValueAsString(getAnnotatedDogInZoo());
        System.out.println(json);
        System.out.println();
    }
    
    @Test
    public void testObjectMapperWithNonFinalDefaultTypingSerializeAnnotatedEmptyZoo() throws Exception{
        System.out.println("testObjectMapperWithNonFinalDefaultTypingSerializeAnnotatedEmptyZoo");
        ObjectMapper objectMapper = getObjectMapperWithNonFinalDefaultTyping();
        String json = objectMapper.writeValueAsString(getAnnotatedEmptyZoo());
        System.out.println(json);
        System.out.println();
    }
    
    @Test
    public void testObjectMapperWithNonFinalDefaultTypingSerializeAnnotatedDogInZoo() throws Exception{
        System.out.println("testObjectMapperWithNonFinalDefaultTypingSerializeAnnotatedDogInZoo");
        ObjectMapper objectMapper = getObjectMapperWithNonFinalDefaultTyping();
        String json = objectMapper.writeValueAsString(getAnnotatedDogInZoo());
        System.out.println(json);
        System.out.println();
    }
    
    @JsonTypeInfo(use=JsonTypeInfo.Id.CLASS, include=JsonTypeInfo.As.PROPERTY, property="@class")
    public static class ZooWithAnnotatedAnimalList{
        public List<AnnotatedAnimal> animals = new ArrayList<>();
    }
    
    public ZooWithAnnotatedAnimalList getZooWithAnnotatedCatAndDogList(){
        ZooWithAnnotatedAnimalList zoo = new ZooWithAnnotatedAnimalList();
        AnnotatedDog dog = new AnnotatedDog();
        dog.name = "fido";
        dog.barkVolume = 90;
        zoo.animals.add(dog);
        AnnotatedCat cat = new AnnotatedCat();
        cat.name = "garfield";
        cat.lives = 9;
        cat.likesCream = true;
        zoo.animals.add(cat);
        return zoo;
    }
    
    @Test
    public void testDefaultObjectMapperSerializeZooWithCatAndDogList() throws Exception{
        System.out.println("testDefaultObjectMapperSerializeZooWithCatAndDogList");
        ObjectMapper objectMapper = getDefaultObjectMapper();
        String json = objectMapper.writeValueAsString(getZooWithAnnotatedCatAndDogList());
        System.out.println(json);
        System.out.println();
    }
    
    @Test
    public void testObjectMapperWithDefaultTypingSerializeZooWithCatAndDogList() throws Exception{
        System.out.println("testObjectMapperWithDefaultTypingSerializeZooWithCatAndDogList");
        ObjectMapper objectMapper = getObjectMapperWithDefaultTyping();
        String json = objectMapper.writeValueAsString(getZooWithAnnotatedCatAndDogList());
        System.out.println(json);
        System.out.println();
    }
    
    @Test
    public void testObjectMapperWithNonFinalDefaultTypingSerializeZooWithCatAndDogList() throws Exception{
        System.out.println("testObjectMapperWithNonFinalDefaultTypingSerializeAnnotatedDogInZooList");
        ObjectMapper objectMapper = getObjectMapperWithNonFinalDefaultTyping();
        String json = objectMapper.writeValueAsString(getZooWithAnnotatedCatAndDogList());
        System.out.println(json);
        System.out.println();
    }
    
    @JsonTypeInfo(use=JsonTypeInfo.Id.CLASS, include=JsonTypeInfo.As.PROPERTY, property="@class")
    public static class ZooWithAnnotatedAnimalArray{
        public AnnotatedAnimal[] animals = new AnnotatedAnimal[2];
    }
    
    public ZooWithAnnotatedAnimalArray getZooWithAnnotatedCatAndDogArray(){
        ZooWithAnnotatedAnimalArray zoo = new ZooWithAnnotatedAnimalArray();
        AnnotatedDog dog = new AnnotatedDog();
        dog.name = "fido";
        dog.barkVolume = 90;
        zoo.animals[0] = dog;
        AnnotatedCat cat = new AnnotatedCat();
        cat.name = "garfield";
        cat.lives = 9;
        cat.likesCream = true;
        zoo.animals[1] = cat;
        return zoo;
    }
    
    @Test
    public void testDefaultObjectMapperSerializeZooWithCatAndDogArray() throws Exception{
        System.out.println("testDefaultObjectMapperSerializeZooWithCatAndDogArray");
        ObjectMapper objectMapper = getDefaultObjectMapper();
        String json = objectMapper.writeValueAsString(getZooWithAnnotatedCatAndDogArray());
        System.out.println(json);
        System.out.println();
    }
    
    @Test
    public void testObjectMapperWithDefaultTypingSerializeZooWithCatAndDogArray() throws Exception{
        System.out.println("testObjectMapperWithDefaultTypingSerializeZooWithCatAndDogArray");
        ObjectMapper objectMapper = getObjectMapperWithDefaultTyping();
        String json = objectMapper.writeValueAsString(getZooWithAnnotatedCatAndDogArray());
        System.out.println(json);
        System.out.println();
    }
    
    @Test
    public void testObjectMapperWithNonFinalDefaultTypingSerializeZooWithCatAndDogArray() throws Exception{
        System.out.println("testObjectMapperWithNonFinalDefaultTypingSerializeAnnotatedDogInZooArray");
        ObjectMapper objectMapper = getObjectMapperWithNonFinalDefaultTyping();
        String json = objectMapper.writeValueAsString(getZooWithAnnotatedCatAndDogArray());
        System.out.println(json);
        System.out.println();
    }
    
    private ObjectMapper getObjectMapperWithNonFinalDefaultTyping2(){
        return new ObjectMapper().enableDefaultTyping(DefaultTyping.NON_FINAL, As.PROPERTY);
    }
    
    @Test
    public void testObjectMapperWithNonFinalDefaultTyping2SerializeDogInZoo() throws Exception{
        System.out.println("testObjectMapperWithNonFinalDefaultTyping2SerializeDogInZoo");
        ObjectMapper objectMapper = getObjectMapperWithNonFinalDefaultTyping2();
        String json = objectMapper.writeValueAsString(getDogInZoo());
        System.out.println(json);
        System.out.println();
    }
    
    @Test
    public void testObjectMapperWithNonFinalDefaultTyping2SerializeZooWithCatAndDogArray() throws Exception{
        System.out.println("testObjectMapperWithNonFinalDefaultTypingSerializeAnnotatedDogInZooArray");
        ObjectMapper objectMapper = getObjectMapperWithNonFinalDefaultTyping2();
        String json = objectMapper.writeValueAsString(getZooWithAnnotatedCatAndDogArray());
        System.out.println(json);
        System.out.println();
    }
    
    @Test
    public void testObjectMapperWithNonFinalDefaultTyping2SerializeZooWithCatAndDogList() throws Exception{
        System.out.println("testObjectMapperWithNonFinalDefaultTypingSerializeAnnotatedDogInZooList");
        ObjectMapper objectMapper = getObjectMapperWithNonFinalDefaultTyping2();
        String json = objectMapper.writeValueAsString(getZooWithAnnotatedCatAndDogList());
        System.out.println(json);
        System.out.println();
    }
    
    @JsonTypeInfo(use=JsonTypeInfo.Id.CLASS, include=JsonTypeInfo.As.PROPERTY, property="@class")
    public static class ZooWithAnimalList{
        public List<Animal> animals = new ArrayList<>();
    }
    
    public ZooWithAnimalList getZooWithCatAndDogList(){
        ZooWithAnimalList zoo = new ZooWithAnimalList();
        Dog dog = new Dog();
        dog.name = "fido";
        dog.barkVolume = 90;
        zoo.animals.add(dog);
        Cat cat = new Cat();
        cat.name = "garfield";
        cat.lives = 9;
        cat.likesCream = true;
        zoo.animals.add(cat);
        return zoo;
    }
    
    public ObjectMapper getObjectMapperWithNonFinalDefaultTyping3(){
        return new ObjectMapper()
                .enableDefaultTyping(DefaultTyping.NON_FINAL, As.PROPERTY)
                .setVisibility(PropertyAccessor.FIELD, Visibility.ANY)
                .setVisibility(PropertyAccessor.GETTER, Visibility.NONE)
                .setVisibility(PropertyAccessor.IS_GETTER, Visibility.NONE)
                .setVisibility(PropertyAccessor.SETTER, Visibility.NONE);
    }
    
    @Test
    public void testObjectMapperWithNonFinalDefaultTyping3SerializeZooWithCatAndDogList() throws Exception{
        System.out.println("testObjectMapperWithNonFinalDefaultTyping3SerializeZooWithCatAndDogList");
        ObjectMapper objectMapper = getObjectMapperWithNonFinalDefaultTyping3();
        String json = objectMapper.writeValueAsString(getZooWithCatAndDogList());
        System.out.println(json);
        System.out.println();
    }
    
    @JsonTypeInfo(use=JsonTypeInfo.Id.CLASS, include=JsonTypeInfo.As.PROPERTY, property="@class")
    public static class ZooWithAnimalArray{
        public Animal[] animals = new Animal[2];
    }
    
    public ZooWithAnimalArray getZooWithCatAndDogArray(){
        ZooWithAnimalArray zoo = new ZooWithAnimalArray();
        Dog dog = new Dog();
        dog.name = "fido";
        dog.barkVolume = 90;
        zoo.animals[0] = dog;
        Cat cat = new Cat();
        cat.name = "garfield";
        cat.lives = 9;
        cat.likesCream = true;
        zoo.animals[1] = cat;
        return zoo;
    }
    
    @Test
    public void testObjectMapperWithNonFinalDefaultTyping3SerializeZooWithCatAndDogArray() throws Exception{
        System.out.println("testObjectMapperWithNonFinalDefaultTyping3SerializeZooWithCatAndDogArray");
        ObjectMapper objectMapper = getObjectMapperWithNonFinalDefaultTyping3();
        String json = objectMapper.writeValueAsString(getZooWithCatAndDogArray());
        System.out.println(json);
        System.out.println();
    }

}
