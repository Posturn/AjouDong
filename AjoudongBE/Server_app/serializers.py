from rest_framework import serializers, viewsets
from Server_app.models import userAccount
from Server_app.models import major_Affiliation
from Server_app.models import club

class MajorSerializer(serializers.ModelSerializer):
    class Meta:
        model = major_Affiliation
        fields=('majorName', 'majorCollege', )

class ClubSerializer(serializers.ModelSerializer):
    class Meta:
        model = club
        fields=('clubID','clubName','clubCategory','clubIMG','clubMajor','clubDues')