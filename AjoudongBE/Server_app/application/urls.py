from django.contrib import admin
from django.urls import path, include, re_path
from django.views.decorators.csrf import csrf_exempt

from rest_framework import routers

from . import application

urlpatterns = [
    path('/result/<int:uSchoolID>', csrf_exempt(application.getApplicationResult.as_view())),
    path('/result/<int:uSchoolID>/deleteApplication/<int:clubID>', csrf_exempt(application.deleteApplication.as_view())),
    path('/clubapplyacitve/<int:clubID>', application.getClubApplyActive.as_view({"get":"retrieve"})),
    path('/applicationrecord/<int:clubID>/<int:uSchoolID>', application.getApplyRecord.as_view({"get":"retrieve"})),
]