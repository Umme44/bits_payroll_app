import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { IMobileBill } from '../legacy-model/mobile-bill.model';
import { ApplicationConfigService } from '../../../core/config/application-config.service';
import { createRequestOption } from '../../../core/request/request-util';

type EntityResponseType = HttpResponse<IMobileBill>;
type EntityArrayResponseType = HttpResponse<IMobileBill[]>;

@Injectable({ providedIn: 'root' })
export class MobileBillService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/payroll-mgt/mobile-bills');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(mobileBill: IMobileBill): Observable<EntityResponseType> {
    return this.http.post<IMobileBill>(this.resourceUrl, mobileBill, { observe: 'response' });
  }

  update(mobileBill: IMobileBill): Observable<EntityResponseType> {
    return this.http.put<IMobileBill>(this.resourceUrl, mobileBill, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IMobileBill>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IMobileBill[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  queryWithParams(year: number, month: number, req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IMobileBill[]>(`${this.resourceUrl}/${year}/${month}`, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  uploadXlsxFile(file: File, year: number, month: number): Observable<HttpResponse<boolean>> {
    const formData: FormData = new FormData();
    formData.append('file', file);
    return this.http.post<boolean>(`${this.resourceUrl}/xlsx-upload/${year}/${month}`, formData, { observe: 'response' });
  }
}
