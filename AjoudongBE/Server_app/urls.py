from django.contrib import admin
from django.urls import path, include
from rest_framework import routers
from Server_app import views



app_name='Server_app'
urlpatterns=[
    path('', include('rest_framework.urls', namespace='rest_framework_category')),
    
   ]
