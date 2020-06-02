from django.contrib import admin
from django.urls import path, include, re_path
from django.views.decorators.csrf import csrf_exempt

from rest_framework import routers

from . import application

urlpatterns = [
    path('/result/<int:uSchoolID>', csrf_exempt(application.getApplicationResult.as_view())),
    # path('/application/result/<int:uSchoolID>/deleteApplication/<int:clubID>', csrf_exempt(application.deleteApplication.as_view())),
]