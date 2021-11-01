
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { GenericApiService } from 'src/app/common/shared';

@Injectable()
export class ShareApiService<T> extends GenericApiService<T> {
  constructor(protected http: HttpClient, public resourceUrl: string) {
    super(http, resourceUrl);
  }

  getPublishedVersion(id: number): Observable<T> {
    return this.http.get<T>(`${this.url}/${id}/getPublishedVersion`).pipe(catchError(this.handleError));
  }

  publish(id: number): Observable<any> {
    return this.http.put(`${this.url}/${id}/publish`, {}).pipe(catchError(this.handleError));
  }

  refresh(id: number): Observable<any> {
    return this.http.put(`${this.url}/${id}/refresh`, {}).pipe(catchError(this.handleError));
  }

}
