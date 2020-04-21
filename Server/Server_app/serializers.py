from rest_framework import serializers
from Server_app.models import Test

class TestSerializer(serializers.ModelSerializer):
    class Meta:
        model=Test
        fields=('test', )