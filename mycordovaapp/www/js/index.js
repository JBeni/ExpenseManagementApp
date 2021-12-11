/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

// Wait for the deviceready event before using any of Cordova's device APIs.
// See https://cordova.apache.org/docs/en/latest/cordova/events/events.html#deviceready
document.addEventListener('deviceready', onDeviceReady, false);

function onDeviceReady() {
    // Cordova is now initialized. Have fun!

    console.log('Running cordova-' + cordova.platformId + '@' + cordova.version);
    document.getElementById('deviceready').classList.add('ready');

    alert('Model: ' + device.model + '\n' +
        'Cordova: ' + device.cordova + '\n' +
        'Platform: ' + device.platform + '\n' +
        'Build: ' + device.uuid + '\n' +
        'Version: ' + device.version);
}

var localstorage_value=document.getElementById("localstorage_value")
var delete_data=document.getElementById("delete_data")
var read_data=document.getElementById("read_data")
var edit_data=document.getElementById("edit_data")

read_data.onclick=function() {
    localstorage_value.textContent=localStorage.getItem("name", "destination", "date",
     "risk_assessment", "duration", "aim", "status");
}

delete_data.onclick=function() {
    localStorage.removeItem("name", "destination", "date",
     "risk_assessment", "duration", "aim", "status");
}

edit_data.onclick=function() {
    localStorage.editItem("name", "destination", "date",
     "risk_assessment", "duration", "aim", "status");
}


function onDeviceReady() {
    document.getElementById("saveButton").addEventListener("click", storeName, storeDestination, storeDate,
        storeRiskAssessment, storeDuration, storeAim, storeStatus);
}


function storeName() {
    var myName = document.getElementById("name").value;
    window.localStorage.setItem("name", myName);
    return false;
}

function storeDestination() {
    var myDestination = document.getElementById("destination").value;
    window.localStorage.setItem("destination", myDestination);
    return false;
}

function storeDate() {
    var myDate = document.getElementById("date").value;
    window.localStorage.setItem("date", myDate);
    return false;
}

function storeRiskAssessment() {
    var myRiskAssessment = document.getElementById("risk_assessment").value;
    window.localStorage.setItem("risk_assessment", myRiskAssessment);
    return false;
}

function storeDuration() {
    var myDuration = document.getElementById("duration").value;
    window.localStorage.setItem("duration", myDuration);
    return false;
}

function storeAim() {
    var myAim = document.getElementById("aim").value;
    window.localStorage.setItem("aim", myAim);
    return false;
}

function storeStatus() {
    var myStatus = document.getElementById("status").value;
    window.localStorage.setItem("status", myStatus);
    return false;
}



