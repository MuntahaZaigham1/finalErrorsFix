
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { T1Service } from 'src/app/entities/t-1/t-1.service';
@Injectable({
  providedIn: 'root'
})
export class T1ExtendedService extends T1Service {
	constructor(protected httpclient: HttpClient) { 
    super(httpclient);
    this.url += '/extended';
  }
}
