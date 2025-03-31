package codingweek.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


class Root {
    @JsonProperty("categories")
    public Map<String, Category> categories;
}

class Category {
    @JsonProperty("mots")
    public List<Word> mots;
}

class Word {
    @JsonProperty("mot")
    public String mot;

    @JsonProperty("interdits")
    public List<String> interdits;

    @JsonProperty("chemin")
    public String chemin;
}

public class JsonReader {
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static ArrayList<Card> jsonReader(String filePath, String category, boolean imagesMode) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        Path filePathPath = Paths.get(filePath);
        InputStream inputStream = Files.newInputStream(filePathPath);

        if (inputStream == null) {
            throw new IllegalArgumentException("File not found: " + filePath);
        }

        Root root = objectMapper.readValue(inputStream, Root.class);

        ArrayList<Card> cardList = new ArrayList<>();
        Card card;
        Category cat = root.categories.get(category);
        if (cat != null) {
            for (Word word : cat.mots) {
                if (imagesMode && word.chemin != null) {
                    card = new Card(word.chemin, Card.NEUTRAL_COLOR);
                } else {
                    card = new Card(word.mot, Card.NEUTRAL_COLOR);
                }
                card.addForbiddenWords((ArrayList) word.interdits);
                cardList.add(card);
            }
        } else {
            throw new IllegalArgumentException("Category not found: " + category);
        }
        return cardList;
    }

    // Méthode pour récupérer la liste des catégories du fichier JSON
    public static Map<String, Category> getCategories(String filePath) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        Path filePathPath = Paths.get(filePath);
        InputStream inputStream = Files.newInputStream(filePathPath);

        if (inputStream == null) {
            throw new IllegalArgumentException("File not found: " + filePath);
        }

        Root root = objectMapper.readValue(inputStream, Root.class);
        return root.categories;
    }   

    // Méthode pour ajouter un mot à une catégorie et réécrire le fichier JSON
    public static boolean addWordToCategory(String filePath, String categoryName, String newWord) throws IOException {

        ObjectMapper objectMapper = new ObjectMapper();

        // Vérifier que le mot contient uniquement des lettres
        if (!newWord.matches("^[a-zA-ZÀ-ÖØ-öø-ÿ]+$")) {
            showError("Validation error", "The word can only contains letter!");
            return false;
        }
        
        Path filePathPath = Paths.get(filePath);
        InputStream inputStream = Files.newInputStream(filePathPath);

        if (inputStream == null) {
            throw new IllegalArgumentException("File not found: " + filePath);
        }

        Root root = objectMapper.readValue(inputStream, Root.class);

        // Récupérer la catégorie
        Category category = root.categories.get(categoryName);
        if (category != null) {
            // Vérifier si le mot existe déjà dans la catégorie
            for (Word existingWord : category.mots) {
                if (existingWord.mot.equalsIgnoreCase(newWord)) {
                    showError("Add error", "The word already exist in this category !");
                    return false; // Retourner false si le mot est déjà présent
                }
            }

            // Créer un nouveau mot et l'ajouter à la catégorie
            Word word = new Word();
            word.mot = newWord;
            word.interdits = new ArrayList<>();
            word.chemin = null; // Vous pouvez ajouter un chemin ici si nécessaire
            category.mots.add(word);

            // Réécrire les catégories dans le fichier JSON
            // ressourcePath = JsonReader.class.getClassLoader().getResource("").getPath();
            try (FileWriter fileWriter = new FileWriter(filePath)) {
                objectMapper.writeValue(fileWriter, root);
            }
            return true;
        } else {
            showError("Category error", "The specified category was not found!");
            return false;
        }
    }

    // Méthode pour ajouter une nouvelle catégorie et réécrire le fichier JSON
    public static boolean addCategory(String filePath, String newCategoryName) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();

        // Vérifier que le nom de la catégorie contient uniquement des lettres
        if (!newCategoryName.matches("^[a-zA-ZÀ-ÖØ-öø-ÿ\\s]+$")) {
            showError("Validation error", "The word can only contains letter!");
            return false;
        }

        Path filePathPath = Paths.get(filePath);
        InputStream inputStream = Files.newInputStream(filePathPath);

        if (inputStream == null) {
            throw new IllegalArgumentException("File not found: " + filePath);
        }

        Root root = objectMapper.readValue(inputStream, Root.class);

        // Vérifier si la catégorie existe déjà
        if (root.categories.containsKey(newCategoryName)) {
            showError("Add error", "This category already exists!");
            return false;
        }

        // Ajouter une nouvelle catégorie
        Category newCategory = new Category();
        newCategory.mots = new ArrayList<>();
        root.categories.put(newCategoryName, newCategory);

        // Réécrire les catégories dans le fichier JSON
        try (FileWriter fileWriter = new FileWriter(filePath)) {
            objectMapper.writeValue(fileWriter, root);
        }

        return true;
    }

    private static void showError(String title, String content) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
    
    public static int getCategoryWordCount(String category) {
        try {
            // Charger les catégories à partir du fichier JSON
            Map<String, Category> categories = JsonReader.getCategories("mots.json");
    
            // Récupérer la catégorie spécifiée
            Category cat = categories.get(category);
    
            // Vérifier si la catégorie existe et renvoyer le nombre de mots
            return cat != null ? cat.mots.size() : 0;
    
        } catch (IOException e) {
            showError("Error", "Can't read JSON file");
            return 0; // Retourner 0 en cas d'erreur
        }
    }
    
}
