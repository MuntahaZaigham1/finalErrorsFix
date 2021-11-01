
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { T2Service } from 'src/app/entities/t-2/t-2.service';
@Injectable({
  providedIn: 'root'
})
export class T2ExtendedService extends T2Service {
	constructor(protected httpclient: HttpClient) { 
    super(httpclient);
    this.url += '/extended';
  }
}
