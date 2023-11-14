package com.example.filmboock.base;

import com.example.filmboock.HomeWindow;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class Base {
    private final static String PATH_DATA = "DATA/base.json";
    private final static String PATH_GENRE = "DATA/genre.json";
    private final static String PATH_YEAR = "DATA/year.json";
    private final static String PATH_ACTOR = "DATA/actor.json";
    private final static String PATH_COLLECTION = "DATA/collection.json";
    private List<Film> filmList = new ArrayList<>();
    private Map<String, String> genreList = new HashMap<>();
    private Map<String, String> actorList = new HashMap<>();
    private Map<String, String> yearList = new HashMap<>();
    private Map<String, String> collectionList = new HashMap<>();
    private int startId = 0;
    HomeWindow homeWindow = null;

    public Base() {
        loadBase();
        genreList = loadMap(PATH_GENRE);
        actorList = loadMap(PATH_ACTOR);
        yearList = loadMap(PATH_YEAR);
        collectionList = loadMap(PATH_COLLECTION);
    }
    //id image name year genre description actor collection
    public void writer(String image, String name, String year, List<String> genre,
                       String description, List<String> actor, String collection) {
        Film newFilm = new Film(startId, name, image, year, genre, description, actor, collection);
        filmList.add(newFilm);
        reWriteFile(genre,actor,year,collection);
        startId++;
    }
    public void reWriteFilm(int id, String image, String name, String year, List<String> genre,
                        String description, List<String> actor, String collection){
        int indexFilm = 0;
        for(Film film:filmList){
            if(film.getId()==id){
                indexFilm = film.getId();
                break;
            }
        }
        filmList.get(indexFilm).setImage(image);
        filmList.get(indexFilm).setName(name);
        filmList.get(indexFilm).setYear(year);
        filmList.get(indexFilm).setGenre(genre);
        filmList.get(indexFilm).setDescription(description);
        filmList.get(indexFilm).setActor(actor);
        filmList.get(indexFilm).setCollection(collection);
        reWriteFile(genre,actor,year,collection);
    }
    private void reWriteFile(List<String> genre,List<String> actor,String year,String collection){
        fileWrite(PATH_DATA, filmList);
        //addGenre
        addMapInfo(genre, genreList);
        fileWrite(PATH_GENRE, genreList);
        //addActor
        addMapInfo(actor, actorList);
        fileWrite(PATH_ACTOR, actorList);
        //addYear
        addMapInfo(year, yearList);
        fileWrite(PATH_YEAR, yearList);
        //addCollection
        addMapInfo(collection, collectionList);
        fileWrite(PATH_COLLECTION, collectionList);
    }

    public List<Film> getAllFilms() {
        return filmList;
    }

    public Map<String, String> getGenreList() {
        return genreList;
    }

    public Map<String, String> getYearList() {
        return yearList;
    }

    public List<Film> getFilmToGenre(String genre) {
        List<Film> result = new ArrayList<>();
        for (Film film : filmList) {
            for (String genres : film.getGenre()) {
                if (genres.equals(genre)) {
                    result.add(film);
                    break;
                }
            }
        }
        return result;
    }

    public List<Film> getFilmToActor(String actor) {
        List<Film> result = new ArrayList<>();
        for (Film film : filmList) {
            for (String actors : film.getActor()) {
                if (actors.equals(actor)) {
                    result.add(film);
                    break;
                }
            }
        }
        return result;
    }

    public List<Film> getFilmToYear(String year) {
        List<Film> result = new ArrayList<>();
        for (Film film : filmList) {
            if (film.getYear().equals(year)) {
                result.add(film);
            }
        }
        return result;
    }

    public List<Film> getFilmToName(String name) {
        List<Film> result = new ArrayList<>();
        for (Film film : filmList) {
            if (film.name.trim().toLowerCase().contains(name.trim().toLowerCase()) && !name.isEmpty()) {
                result.add(film);
            }
        }
        return result;
    }

    public Film getFilmToId(int id) {
        Film result = null;
        for (Film film : filmList) {
            if (film.getId() == id) {
                result = film;
                break;
            }
        }
        return result;
    }

    public List<Film> getFilmToCollection(String collection) {
        List<Film> result = new ArrayList<>();
        for (Film film : filmList) {
            if (film.getCollection().equals(collection) && !collection.isEmpty()) {
                result.add(film);
            }
        }
        result.sort(new Comparator<Film>() {
            @Override
            public int compare(Film o1, Film o2) {
                if (Integer.parseInt(o1.getYear()) < Integer.parseInt(o2.getYear())) {
                    return -1;
                } else {
                    return 1;
                }
            }
        });
        return result;
    }

    public List<String> getCollectionToValue(String value) {
        List<String> result = new ArrayList<>();
        for (String collections : collectionList.keySet()) {
            if (collections.toLowerCase().contains(value.toLowerCase().trim()) && !value.isEmpty()) {
                result.add(collections);
            }
        }
        return result;
    }

    public void removeFilm(int id) {
        File fileImg = null;
        collectionList = new HashMap<>();
        genreList = new HashMap<>();
        actorList = new HashMap<>();
        yearList = new HashMap<>();
        int removeId = -1;
        for (int i = 0; i < filmList.size(); i++) {
            if(filmList.get(i).getId()==id){
                removeId = i;
                fileImg = new File(filmList.get(i).getImage());
            }
            if(filmList.get(i).getId()!=id){
                collectionList.put(filmList.get(i).getCollection(),"");
                yearList.put(filmList.get(i).getYear(),"");
                for(String genres:filmList.get(i).getGenre()){
                    genreList.put(genres,"");
                }
                for(String actors:filmList.get(i).getActor()){
                    actorList.put(actors,"");
                }
            }
        }
        if(removeId>=0) {
            filmList.remove(removeId);
            fileWrite(PATH_DATA, filmList);
            fileWrite(PATH_GENRE, genreList);
            fileWrite(PATH_ACTOR, actorList);
            fileWrite(PATH_YEAR, yearList);
            fileWrite(PATH_COLLECTION, collectionList);

        }
    }
    private void addMapInfo(List<String> list, Map<String, String> map) {
        for (String genre : list) {
            map.put(genre, "");
        }
    }

    private void addMapInfo(String value, Map<String, String> map) {
        if(!value.isEmpty()) {
            map.put(value, "");
        }
    }

    private void fileWrite(String path, List<?> list) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(list);
        try {
            FileOutputStream fos = new FileOutputStream(path);
            fos.write(json.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void fileWrite(String path, Map<String, String> list) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(list);
        try {
            FileOutputStream fos = new FileOutputStream(path);
            fos.write(json.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Map<String, String> loadMap(String path) {
        File fileData = new File(path);
        StringBuilder textFileData = new StringBuilder();

        try (FileInputStream fis = new FileInputStream(fileData);
             Scanner scanner = new Scanner(fis)
        ) {
            while (scanner.hasNext()) {
                textFileData.append(scanner.nextLine()).append("\n");
            }
            if (textFileData.toString().length() > 0) {
                return new Gson().fromJson(textFileData.toString(), Map.class);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return new HashMap<>();
    }

    private void loadBase() {
        File fileData = new File(PATH_DATA);
        StringBuilder textFileData = new StringBuilder();

        try (FileInputStream fis = new FileInputStream(fileData);
             Scanner scanner = new Scanner(fis)
        ) {
            while (scanner.hasNext()) {
                textFileData.append(scanner.nextLine()).append("\n");
            }
            if (textFileData.toString().length() > 0) {

                Type listOfMyClassObject = new TypeToken<ArrayList<Film>>() {
                }.getType();
                filmList = new Gson().fromJson(textFileData.toString(), listOfMyClassObject);
                for (Film film : filmList) {
                    if (film.id > startId) {
                        startId = film.id;
                    }
                    startId++;
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
