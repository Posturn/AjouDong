from rest_framework import serializers, viewsets
from Server_app.models import UserAccount, Club, ClubActivity, ClubPromotion, Major_Affiliation

class clubPromotionSerializer(serializers.ModelSerializer):
    class Meta:
        model = ClubPromotion
        fields = ('posterIMG', 'clubInfo', 'clubFAQ', 'clubApply', 'clubContact', 'additionalApply', 'recruitStartDate', 'recruitEndDate')

class clubActivitySerializer(serializers.ModelSerializer):
    class Meta:
        model = ClubActivity
        fields = ('clubActivityFile', 'clubActivityInfo', 'clubActivityID','clubID','clubActivityDetail')
        
class MajorSerializer(serializers.ModelSerializer):
    class Meta:
        model = Major_Affiliation
        fields=('majorName', 'majorCollege', )

class ClubSerializer(serializers.ModelSerializer):
    class Meta:
        model = Club
        fields=('clubID','clubName','clubCategory','clubIMG','clubMajor','clubDues')

