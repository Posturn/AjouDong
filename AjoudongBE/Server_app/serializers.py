from rest_framework import serializers
from Server_app.models import userAccount

class TestSerializer(serializers.ModelSerializer):
    class Meta:
        model=userAccount
        fields=('uID', 'uPW', )