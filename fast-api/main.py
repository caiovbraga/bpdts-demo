import requests
import json
import math

from fastapi import FastAPI

demo_api = FastAPI()

@demo_api.get("/")
async def root():
  return {"message":"Demo FastAPI"}

@demo_api.get("/instructions")
async def instructions():
  r = requests.get("http://bpdts-test-app-v4.herokuapp.com/instructions")
  return r.json()

@demo_api.get("/users")
async def users():
  r = requests.get("http://bpdts-test-app-v4.herokuapp.com/users")
  print(len(r.json()))
  return r.json()

@demo_api.get("/user/{user_id}")
async def user(user_id: int):
  url = "http://bpdts-test-app-v4.herokuapp.com/user/" + str(user_id)
  r = requests.get(url)
  return r.json()

@demo_api.get("/city/{city}/users")
async def user(city):
  url = "http://bpdts-test-app-v4.herokuapp.com/city/" + city + "/users"
  r = requests.get(url)
  return r.json()

@demo_api.get("/london") # Return users city=London
async def get_london_users():
  city = "London"
  url = "http://bpdts-test-app-v4.herokuapp.com/city/" + city + "/users"
  r = requests.get(url)
  return r.json()
  
# https://stackoverflow.com/questions/7477003/calculating-new-longitude-latitude-from-old-n-meters/7478827#7478827
#
# The number of kilometers per degree of longitude is approximately (2*pi/360) * r_earth * cos(theta)
# where theta is the latitude in degrees and r_earth is approximately 6378 km.
#
#The number of kilometers per degree of latitude is approximately the same at all locations, approx
# (2*pi/360) * r_earth = 111 km / degree 
#
# London
# Latitude 51.507247 Logitude -0.127716
# Radius 60 miles, 96.5606 kilometers
  
@demo_api.get("/within-london-radius")
async def get_radius_london_users():
  radius_from_london = 96.5606 #kilometers, 60 miles

  lat_degree_60_miles = radius_from_london / 111 # 111km aprox 1 latitude degree 
  london_latitude = 51.507247
  max_latitude_north = london_latitude + lat_degree_60_miles
  max_latitude_south = london_latitude - lat_degree_60_miles

  london_longitude = -0.127716 
  r_earth = 6371 #kilometers
  long_degree_km = ( (2*math.pi)/360 ) * r_earth * math.cos(london_latitude) #per longitude degree
  long_degree_60_miles = radius_from_london / long_degree_km
  max_longitude_east = london_longitude + long_degree_60_miles
  max_longitude_west = london_longitude + ( long_degree_60_miles * -1 )
  
  url = "http://bpdts-test-app-v4.herokuapp.com/users"
  r = requests.get(url)
  json_object = r.json()
  
  users_london_range = []
  for key in json_object:
    user_dict = key
    if ( max_latitude_south <= float(user_dict["latitude"]) <= max_latitude_north ) and ( max_longitude_west <= float(user_dict["longitude"]) <= max_longitude_east) :
      print(user_dict["id"], user_dict["first_name"], user_dict["latitude"], user_dict["longitude"])
      users_london_range.append(json.dumps(user_dict))

  return users_london_range