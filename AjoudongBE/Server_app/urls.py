from django.contrib import admin
from django.urls import path, include

app_name='Server_app'
urlpatterns=[
    path('', include('rest_framework.urls', namespace='rest_framework_category')),]
