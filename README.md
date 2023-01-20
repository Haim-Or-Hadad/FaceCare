# FaceCare
this repo conatains facecare application    
## prerequisets:
* android studio 
* facecare-backend-js-server running - (https://github.com/RazGavrieli/facecare-backend-js-server)
### about 
faceCare app is a generic & scalable appointment scheduling app.
Allows users to easily connect and authenticate through SMS code and schedule an appointment quickly. 
Allows the admin to view and manage future appointments, configure types of available services and set working days. 
### System Infrastructure
![Screenshot_6](https://user-images.githubusercontent.com/93033782/213639592-f910592e-aede-4ab0-9068-1805c91f3073.png)
### UML 
![Screenshot_7](https://user-images.githubusercontent.com/93033782/213639706-dae0830c-9a85-4f66-a161-c0937170f381.png)
### ERD 
![Screenshot_8](https://user-images.githubusercontent.com/93033782/213639779-c07261d0-1bb3-469c-8497-dc0c9a1efd19.png)
### mvp 
#### MODEL
In our project the ‘dataAccess’ class is the model . the model reading and writing to the database and includes all the operations that related to the business core of the application.
The model receives information from the presenter and stores what is needed in a database. And it pulls information from the database and transfers to the presenter.
#### VIEW 
Model - all the activity
Everything that related to the display , which is actually all the activity in project.
* MainScreen
* AdminLogin
* AdminManageAppointments
* AdminScheduleClients
* AdminServices
* AdminView
* AdminWorkplan
* UserLogin
* HomeActivity
#### PRESENTER
* The presenter is actually the middleman in the system. 
It is between the model and the view and transfers information between them without there being a direct connection between the model and the view .
* In our system the presenter is the class ‘businessLogicPresenter’ . this class takes information from the view and send it to the model (dataAccessModel) .  
### PROJECT MANAGEMENT
![1](https://user-images.githubusercontent.com/93033782/205494229-13728c3c-c0af-47f7-a07c-424f5a7aaf7b.png)
![2](https://user-images.githubusercontent.com/93033782/205494252-6698f531-6f2d-4732-8708-b0d0ddc29894.png)
