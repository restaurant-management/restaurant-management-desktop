package dao;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Acl;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.StorageClient;
import constants.FirebaseConstants;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FirebaseDao {
    private Bucket _bucket;

    public FirebaseDao() throws IOException {
        FileInputStream serviceAccount =
                new FileInputStream(FirebaseConstants.KEY_PATH);

        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setDatabaseUrl(FirebaseConstants.DATABASE_URL)
                .build();
        FirebaseApp fireApp = FirebaseApp.initializeApp(options);

        StorageClient storageClient = StorageClient.getInstance(fireApp);
        _bucket = storageClient.bucket(FirebaseConstants.BUCKET_NAME);
    }

    public String create(String afterUploadFileName, String filePath) throws IOException {
        // Create input stream from file path.
        InputStream file = new FileInputStream(filePath);

        // Get path from file path to get content type of file.
        Path source = Paths.get(filePath);

        // Upload file to server.
        Blob blob = _bucket.create(afterUploadFileName, file, Files.probeContentType(source),
                Bucket.BlobWriteOption.userProject(FirebaseConstants.PROJECT_ID));

        // Make file public.
        blob.getStorage().createAcl(blob.getBlobId(), Acl.of(Acl.User.ofAllUsers(), Acl.Role.READER));

        // Return accessible link
        return blob.getMediaLink();
    }
}
