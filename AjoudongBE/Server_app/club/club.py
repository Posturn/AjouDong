import json
import os
import sys
import csv
import mimetypes
from django.shortcuts import render
from django.views import View
from django.http import HttpResponse, JsonResponse, FileResponse
from django.views.decorators.csrf import csrf_exempt

# Create your views hereee.
from rest_framework import viewsets
from rest_framework.generics import ListAPIView

from Server_app.models import Apply, ClubMember, UserAccount, AppliedClubList, UserAlarm, Club, ClubStatistic, ClubPromotion

from fcm_django.models import FCMDevice
from django.shortcuts import get_object_or_404

class createData(View):
    @csrf_exempt
    def get(self, request):
        pindex = 0
        sindex = 0
        try:
            clubs = Club.objects.all()

            for club in clubs:
                if ClubPromotion.objects.filter(clubID_id = club.clubID).exists():
                    print("Promotion Exists : " + str(club.clubID))
                else:
                    pindex = pindex + 1
                    ClubPromotion.objects.create(
                        promotionID = club.clubID,
                        posterIMG = None,
                        clubInfo = "동아리 소개를 채워주세요.",
                        clubApply = "동아리 지원 요강을 채워주세요.",
                        clubFAQ = "자주묻는 질문을 작성해주세요.",
                        clubContact = "동아리 간부진 연락처를 알려주세요.",
                        additionalApply = "지원서 추가 질문을 작성해주세요.",
                        recruitStartDate = "2020-03-16",
                        recruitEndDate = "2020-07-02",
                        clubID_id = club.clubID
                    ).save()

                if ClubStatistic.objects.filter(clubID_id = club.clubID).exists():
                    print("Static Exists : " + str(club.clubID))
                else:
                    sindex = sindex + 1
                    ClubStatistic.objects.create(
                        clubID_id = club.clubID,
                        memberNumber = 0,
                        menNumber = 0,
                        womenNumber = 0,
                        overRatio12 = 0,
                        Ratio13 = 0,
                        Ratio14 = 0,
                        Ratio15 = 0,
                        Ratio16 = 0,
                        Ratio17 = 0,
                        Ratio18 = 0,
                        Ratio19 = 0,
                        Ratio20 = 0,
                        engineeringRatio = 0,
                        ITRatio = 0,
                        naturalscienceRatio = 0,
                        managementRatio = 0,
                        humanitiesRatio = 0,
                        socialscienceRatio = 0,
                        nurseRatio = 0,
                        InternationalRatio = 0,
                        DasanRatio = 0,
                        PharmacyRatio = 0,
                        MedicalRatio = 0
                         ).save()
            
            print(pindex)
            print(sindex)

            return JsonResponse({'response' : 1}, status = 200)

        except KeyError:
            return JsonResponse({'response' : -2}, status = 401)