from rest_framework import serializers, viewsets
from Server_app.models import userAccount
from Server_app.models import major_Affiliation

class MajorSerializer(serializers.ModelSerializer):
    class Meta:
        model = major_Affiliation
        fields=('majorName', 'majorCollege', )