from django.shortcuts import render

# Create your views here.
from rest_framework import viewsets

from Server_app.models import Test
from Server_app.serializers import TestSerializer

class TestViewSet(viewsets.ModelViewSet):
    queryset=Test.objects.all()
    serializer_class=TestSerializer