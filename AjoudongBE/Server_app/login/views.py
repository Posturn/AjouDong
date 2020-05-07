from django.shortcuts import render
from django.http import HttpResponse
from django.template import loader

import sys
sys.path.append(os.path.dirname(os.path.abspath(os.path.dirname(__file__))))
from . import models
