import json
import os
import sys
import csv
import mimetypes
from django.shortcuts import render
from django.views import View
from django.http import HttpResponse, JsonResponse, FileResponse
from django.views.decorators.csrf import csrf_exempt

# Create your views here.
from rest_framework import viewsets
from rest_framework.generics import ListAPIView

from Server_app.models import Apply, ClubMember, UserAccount, AppliedClubList, UserAlarm, Club

# class postFAQ(View):
#     @csrf_exempt
#     def post(self, request):
#         try:
        
#         except KeyError:
#             return JsonResponse({'response' : -2}, status = 401)