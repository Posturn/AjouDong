# Generated by Django 3.0.4 on 2020-06-20 12:37

from django.db import migrations, models
import django.db.models.deletion


class Migration(migrations.Migration):

    dependencies = [
        ('Server_app', '0022_auto_20200620_2135'),
    ]

    operations = [
        migrations.DeleteModel(
            name='FAQComment',
        ),
    ]