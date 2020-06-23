from django.contrib import admin
from django.urls import path
from . import views

urlpatterns = [
    path('', views.login, name="login"),
    path('management/', views.management, name="management"),
    path('advertisement/', views.advertisement, name="advertisement"),
    path('addmanageraccount/', views.addmanageraccount, name="addmanageraccount"),
    path('deletemanageraccount/', views.deletemanageraccount, name="deletemanageraccount"),
    path('addclub/', views.addclub, name="addclub"),
    path('clubdelete/', views.clubdelete, name="clubdelete"),
    path('advertisement/addads/', views.addads, name="addadvertisement"),
    path('advertisement/deleteads/', views.deleteads, name="deleteadvertisement"),
    path('advertisement/updateads/', views.updateads, name="updateadvertisement"),
]
