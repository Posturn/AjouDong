from rest_framework import serializers, viewsets
from Server_app.models import userAccount, club, clubActivity, clubPromotion

class clubPromotionSerializer(serializers.ModelSerializer):
    class Meta:
        model = clubPromotion
        fields = ('posterIMG', 'clubInfo', 'clubFAQ', 'clubApply', 'clubContact')

class clubActivitySerializer(serializers.ModelSerializer):
    class Meta:
        model = clubActivity
        fields = ('clubActivityFile', 'clubActivityInfo', 'clubActivityID','clubID_id','clubActivityDetail')