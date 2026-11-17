import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class ProfileStorage {

    private static final String DIRECTORY = "data/profiles";

    public static void saveProfile(PersonalFinanceManager manager) {
        if (manager == null) {
            throw new IllegalArgumentException("Profile cannot be null.");
        }

        try {
            File directory = new File(DIRECTORY);
            directory.mkdirs();

            int userId = manager.getUser().getUserId();
            File file = new File(directory, "profile_" + userId + ".dat");

            ObjectOutputStream output = new ObjectOutputStream(
                new FileOutputStream(file)
            );

            output.writeObject(manager);
            output.close();

            System.out.println("Profile saved successfully!");

        } catch (IOException e) {
            System.out.println("Error saving profile: " + e.getMessage());
        }
    }

    public static PersonalFinanceManager loadProfile(int userId) {
        File file = new File(DIRECTORY, "profile_" + userId + ".dat");

        if (!file.exists()) {
            System.out.println("Profile not found.");
            return null;
        }

        try {
            ObjectInputStream input = new ObjectInputStream(
                new FileInputStream(file)
            );

            PersonalFinanceManager manager =
                (PersonalFinanceManager) input.readObject();

            input.close();

            System.out.println("Profile loaded successfully!");
            return manager;

        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading profile: " + e.getMessage());
            return null;
        }
    }

    public static ArrayList<PersonalFinanceManager> getAllProfiles() {
        ArrayList<PersonalFinanceManager> profiles = new ArrayList<>();

        File directory = new File(DIRECTORY);

        if (!directory.exists()) {
            return profiles;
        }

        File[] files = directory.listFiles();

        if (files == null) {
            return profiles;
        }

        for (File file : files) {
            if (file.isFile()
                && file.getName().startsWith("profile_")
                && file.getName().endsWith(".dat")) {

                try {
                    ObjectInputStream input = new ObjectInputStream(
                        new FileInputStream(file)
                    );

                    PersonalFinanceManager manager =
                        (PersonalFinanceManager) input.readObject();

                    input.close();

                    profiles.add(manager);

                } catch (IOException | ClassNotFoundException e) {
                    System.out.println(
                        "Could not read " + file.getName()
                    );
                }
            }
        }

        return profiles;
    }

    public static int getNextUserId() {
        int highestId = 0;

        ArrayList<PersonalFinanceManager> profiles = getAllProfiles();

        for (PersonalFinanceManager manager : profiles) {
            int id = manager.getUser().getUserId();

            if (id > highestId) {
                highestId = id;
            }
        }

        return highestId + 1;
    }

    public static boolean emailExists(String email) {
        ArrayList<PersonalFinanceManager> profiles = getAllProfiles();

        for (PersonalFinanceManager manager : profiles) {
            if (manager.getUser().getEmail().equalsIgnoreCase(email)) {
                return true;
            }
        }

        return false;
    }
}
