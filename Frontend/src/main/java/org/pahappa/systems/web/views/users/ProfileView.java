package org.pahappa.systems.web.views.users;

import org.apache.commons.io.IOUtils;
import org.pahappa.systems.web.views.HyperLinks;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.CroppedImage;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;
import org.sers.webutils.client.views.presenters.ViewPath;
import org.sers.webutils.client.views.presenters.WebFormView;
import org.sers.webutils.model.Gender;
import org.sers.webutils.model.exception.ValidationFailedException;
import org.sers.webutils.model.security.User;
import org.sers.webutils.server.core.service.UserService;
import org.sers.webutils.server.core.utils.ApplicationContextProvider;
import org.sers.webutils.server.shared.SharedAppData;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@ManagedBean(name = "profileView", eager = true)
@SessionScoped
@ViewPath(path = HyperLinks.PROFILE_VIEW)
public class ProfileView extends WebFormView<User, ProfileView, ProfileView> {

    private static final long serialVersionUID = 1L;
    private CroppedImage croppedImage;
    private String fileName;
    private UploadedFile originalImageFile;
    private UploadedFile imageFile;
    private String imageUrl;
    private String imageString;
    private UserService userService;

    @Override
    public void persist() throws Exception {
        ApplicationContextProvider.getApplicationContext()
                .getBean(UserService.class).saveUser(super.getModel());
    }

    @Override
    public void resetModal() {
        if (super.model == null) {
            super.model = SharedAppData.getLoggedInUser();
        }
        super.resetModal();
        super.model = new User();
    }

    @Override
    @PostConstruct
    public void beanInit() {
        super.model = SharedAppData.getLoggedInUser();
        this.userService = ApplicationContextProvider.getBean(UserService.class);
        this.resetModal();
    }

    @Override
    public void pageLoadInit() {
        if (super.model == null) {
            super.model = SharedAppData.getLoggedInUser();
        }
    }

    @Override
    public String getViewUrl() {
        return this.getViewPath();
    }

//    public String getUserProfile() {
//        super.model = SharedAppData.getLoggedInUser();
//    	return this.getViewPath();
//    }

    public void saveUser() {
        try {
            this.userService.saveUser(super.getModel());
            FacesMessage msg = new FacesMessage("Success", "User updated");
            FacesContext.getCurrentInstance().addMessage(null, msg);

        } catch (ValidationFailedException ex) {
            FacesMessage msg = new FacesMessage("Failed", "Update failed");
            FacesContext.getCurrentInstance().addMessage(null, msg);

            Logger.getLogger(ProfileView.class.getName()).log(Level.SEVERE, null, ex);
        }

    }


    public List<Gender> getAllGenders() {
        return Arrays.asList(Gender.values());
    }

    public User getUserDetails() {
        return SharedAppData.getLoggedInUser();
    }

    /*
    Upload images to cloudinary
     */
    public void fileUploadEvent(FileUploadEvent event) {
        try {
//         Cloudinary   cloudinary = new Cloudinary(ObjectUtils.asMap(
//                    "cloud_name", CustomAppUtils.CLOUDINARY_CLOUD_NAME,
//                    "api_key", CustomAppUtils.CLOUDINARY_API_KEY,
//                    "api_secret", CustomAppUtils.CLOUDINARY_API_SECRET,
//                    "secure", true));

            UploadedFile uploadedFile = event.getFile();
            byte[] contents = IOUtils.toByteArray(uploadedFile.getInputstream());

            System.out.println(Arrays.toString(contents));
//            Map uploadResult = cloudinary.uploader().upload(contents, ObjectUtils.asMap("folder", "california_template_images"));
//            this.imageUrl = uploadResult.get("secure_url").toString();

            this.imageUrl = "admin.png"; //set default image
            System.out.println("Image url = " + imageUrl);
            super.model.setCustomPropOne(this.imageUrl);
            super.model = this.userService.saveUser(super.model);

            FacesMessage msg = new FacesMessage("Successful", event.getFile().getFileName() + " is uploaded.");
            FacesContext.getCurrentInstance().addMessage(null, msg);

        } catch (Exception ex) {
            FacesMessage msg = new FacesMessage("Failed", "Image upload failed");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            Logger.getLogger(ProfileView.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /*
    Upload images to local storage
     */
    public void handleFileUpload(FileUploadEvent event) throws Exception {
        this.originalImageFile = null;
        this.croppedImage = null;
        UploadedFile file = event.getFile();
        System.err.println("Contents: " + file.getFileName());
        System.err.println("Contents: " + Arrays.toString(file.getContents()));

        if (file != null && file.getContents() != null && file.getSize() > 0 && file.getFileName() != null) {
            this.originalImageFile = file;
            FacesContext context = FacesContext.getCurrentInstance();
            ServletContext scontext = (ServletContext) context.getExternalContext().getContext();
            String rootpath = scontext.getRealPath("/");
            File fileImage = new File(rootpath + "upload" + File.separator + "profile_images" + File.separator + "text.png");
            System.err.println("File saved at==========" + fileImage.getAbsolutePath());

            try {
                InputStream inputStream = this.originalImageFile.getInputstream();
                saveImage(inputStream, fileImage);
                this.fileName = super.model.getUsername() + this.fileName;
                super.model.setCustomPropOne(this.fileName);
                persist();
                FacesMessage msg = new FacesMessage("Successful", this.originalImageFile.getFileName() + " is uploaded.");
                FacesContext.getCurrentInstance().addMessage(null, msg);

            } catch (IOException ex) {
                FacesMessage msg = new FacesMessage("Error", this.originalImageFile.getFileName() + " failed to upload.");
                FacesContext.getCurrentInstance().addMessage(null, msg);

                Logger.getLogger(ProfileView.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }

    public void crop() {
        if (this.croppedImage == null || this.croppedImage.getBytes() == null || this.croppedImage.getBytes().length == 0) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Cropping failed."));
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Cropped successfully."));
        }
    }

    public StreamedContent getImage() {

        InputStream inputStream;
        DefaultStreamedContent defaultStreamedContent = new DefaultStreamedContent();

        if (originalImageFile == null
                || originalImageFile.getContents() == null
                || originalImageFile.getSize() == 0) {
            return null;
        }

        try {
            inputStream = new ByteArrayInputStream(originalImageFile.getContents());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        System.err.println("Original: " + originalImageFile.getFileName());
        System.err.println("Contents: " + Arrays.toString(originalImageFile.getContents()));
        System.err.println("Size: " + originalImageFile.getSize());
        defaultStreamedContent.setContentType(originalImageFile.getContentType());
        defaultStreamedContent.setStream(inputStream);
        defaultStreamedContent.setName(originalImageFile.getFileName());
        return defaultStreamedContent;

    }

    public void saveImage(InputStream inputStream, File ImageFile) throws IOException {
        OutputStream outputStream = new FileOutputStream(ImageFile);
        IOUtils.copy(inputStream, outputStream);
    }

    public StreamedContent getCropped() {
        InputStream inputStream;
        DefaultStreamedContent defaultStreamedContent = new DefaultStreamedContent();

        if (this.croppedImage == null
                || this.croppedImage.getBytes() == null
                || this.croppedImage.getBytes().length == 0) {
            return null;
        }

        try {
            inputStream = new ByteArrayInputStream(this.croppedImage.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        defaultStreamedContent.setContentType(originalImageFile.getContentType());
        defaultStreamedContent.setStream(inputStream);
        defaultStreamedContent.setName(originalImageFile.getFileName());
        return defaultStreamedContent;

    }

    public CroppedImage getCroppedImage() {
        return croppedImage;
    }

    public void setCroppedImage(CroppedImage croppedImage) {
        this.croppedImage = croppedImage;
    }

    public UploadedFile getOriginalImageFile() {
        return originalImageFile;
    }

}