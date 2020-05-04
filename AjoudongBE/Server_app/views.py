from django.shortcuts import render

# Create your views here.
from rest_framework import viewsets
from rest_framework.generics import ListAPIView

from Server_app.models import userAccount
from Server_app.serializers import TestSerializer


class TestViewSet(viewsets.ModelViewSet):
    queryset=userAccount.objects.all()
    serializer_class=TestSerializer

class userAccountViewSet(ListAPIView):
    queryset = userAccount.objects.all()
    serializer_class = Testserializer