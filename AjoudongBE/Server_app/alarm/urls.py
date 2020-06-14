from django.contrib import admin
from django.urls import path, include, re_path
from django.views.decorators.csrf import csrf_exempt

from rest_framework import routers

from . import alarm

urlpatterns = [
    path('/stateChange/<int:uSchoolID>', alarm.ChangeStateAlarm.as_view()),
    path('/eventChange/<int:uSchoolID>', alarm.ChangeEventAlarm.as_view()),
    path('/newClubChange/<int:uSchoolID>', alarm.ChangeNewClubAlarm.as_view()),
]