# HealthyZ: Backend for HealthyZ Android App 

[HealthyZ Android App](https://github.com/fs1g17/HealthyApp) is an application that aims to help people develop better eating habits. The application is cloud based, and this project is the backend. Welcome to the cloud :)

Below is a diagram showing the different databases that are used in this project. 

<img src="/images/user_database_model.png" height=500>

The [Food and Nutrient Database for Dietary Studies](https://www.ars.usda.gov/northeast-area/beltsville-md-bhnrc/beltsville-human-nutrition-research-center/food-surveys-research-group/docs/fndds/) (FNDDS) and the [Food Patterns Equivalents Database](https://www.ars.usda.gov/northeast-area/beltsville-md-bhnrc/beltsville-human-nutrition-research-center/food-surveys-research-group/docs/fped-overview/) (FPED) are databases that have been created and managed by the United States Department of Agriculture (USDA). 

MyFitnessPal database is a database that has been generated from [MyFitnessPal dataset](https://www.researchgate.net/publication/305709786_Food_Logging_Dataset). 

## How This Works 

This backend server application allows HealthyZ Android app to connect and upload food diary entries. The entries contain names of the food items consumed. Mongue-Elkan method (shown below) is used to find best match in the USDA databases. These foods are then used to generate vectors representing individual users, and clustering is performed to generate recommendations. The foods from a user's cluster that would maximise their [Healthy Eating Index](https://www.fns.usda.gov/healthy-eating-index-hei) (HEI). 

<img src="/images/string_matching.PNG" width=100%>
