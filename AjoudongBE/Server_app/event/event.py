import json
import os
import sys
from django.shortcuts import render
from django.views import View
from django.http import HttpResponse, JsonResponse
from django.views.decorators.csrf import csrf_exempt

# Create your views here.
from rest_framework import viewsets
from rest_framework.generics import ListAPIView

from Server_app.models import Event, Club

class getEvent(View):
    @csrf_exempt
    def get(self, request, clubID):
        try:
            queryset = Event.objects.filter(clubID_id=clubID).order_by('-eventID')
            List = list(queryset.values())
            eventList = []
            for i in List:
                event = {}
                club = Club.objects.get(clubID = list(i.values())[3])
                event['eventID'] = list(i.values())[0]
                event['eventName'] = list(i.values())[1]
                event['eventDate'] = list(i.values())[2]
                event['clubID']= club.clubID
                event['eventInfo'] = list(i.values())[4]
                event['eventIMG'] = list(i.values())[5]
                #event['eventDate'] = Event.objects.get(clubID_id = list(i.values())[3]).eventDate
                eventList.append(event)

            return JsonResponse({'response' : 1, 'content' : eventList}, status = 200)

        except KeyError:
            return JsonResponse({'response' : -2}, status = 401)

class getEventAll(View):
    @csrf_exempt
    def get(self, request):
        try:
            queryset = Event.objects.all().order_by('-eventID')
            List = list(queryset.values())
            eventList = []
            for i in List:
                event = {}
                club = Club.objects.get(clubID = list(i.values())[3])
                event['eventID'] = list(i.values())[0]
                event['eventName'] = list(i.values())[1]
                event['eventDate'] = list(i.values())[2]
                event['clubID']= club.clubID
                event['eventInfo'] = list(i.values())[4]
                event['eventIMG'] = list(i.values())[5]
                eventList.append(event)

            return JsonResponse({'response' : 1, 'content' : eventList}, status = 200)

        except KeyError:
            return JsonResponse({'response' : -2}, status = 401)