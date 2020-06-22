from django.contrib import admin
from django.urls import path, include, re_path
from django.views.decorators.csrf import csrf_exempt

from rest_framework import routers

from . import FAQ

urlpatterns = [
    path('/postfaq', csrf_exempt(FAQ.postFAQ.as_view())),
    path('/postfaqcomment', csrf_exempt(FAQ.postFAQComment.as_view())),
    path('/getfaq/<int:clubID>', csrf_exempt(FAQ.getFAQ.as_view())),
]