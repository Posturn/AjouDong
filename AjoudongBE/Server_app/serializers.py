from rest_framework import serializers, viewsets
from Server_app.models import UserAccount, Club, ClubActivity, ClubPromotion, Major_Affiliation, MarkedClubList

class clubPromotionSerializer(serializers.ModelSerializer):
    class Meta:
        model = ClubPromotion
        fields = ('posterIMG', 'clubInfo', 'clubFAQ', 'clubApply', 'clubContact')

class clubActivitySerializer(serializers.ModelSerializer):
    class Meta:
        model = ClubActivity
        fields = ('clubActivityFile', 'clubActivityInfo', 'clubActivityID','clubID_id','clubActivityDetail')
        
class MajorSerializer(serializers.ModelSerializer):
    class Meta:
        model = Major_Affiliation
        fields=('majorName', 'majorCollege', )

class ClubSerializer(serializers.ModelSerializer):
    class Meta:
        model = Club
        fields=('clubID','clubName','clubCategory','clubIMG','clubMajor','clubDues')

class BookmarkSerializer(serializers.ModelSerializer):
    class Meta:
        model = MarkedClubList
        fields=('clubID', 'uSchoolID',)

