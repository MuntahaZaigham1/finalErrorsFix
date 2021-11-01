
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { IT2 } from './it-2';
import { GenericApiService } from 'src/app/common/shared';

@Injectable({
  providedIn: 'root'
})
export class T2Service extends GenericApiService<IT2> { 
  constructor(protected httpclient: HttpClient) { 
    super(httpclient, "t2");
  }
  
  convertEnumToArray(enumm:any){
      const arrayObjects = []
        // Retrieve key and values using Object.entries() method.
        for (const [propertyKey, propertyValue] of Object.entries(enumm)) {
         // Add values to array
         arrayObjects.push(propertyValue);
       }

     return arrayObjects;
   }
  
}
