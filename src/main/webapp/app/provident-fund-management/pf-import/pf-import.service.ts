import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ApplicationConfigService } from '../../core/config/application-config.service';

@Injectable({ providedIn: 'root' })
export class PfImportService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/pf-mgt/import');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  pfAccountImport(file: File): Observable<HttpResponse<boolean>> {
    const formData: FormData = new FormData();
    formData.append('file', file);
    return this.http.post<boolean>(this.resourceUrl + `/pf-accounts`, formData, { observe: 'response' });
  }

  previousPfAccountImport(file: File): Observable<HttpResponse<boolean>> {
    const formData: FormData = new FormData();
    formData.append('file', file);
    return this.http.post<boolean>(this.resourceUrl + `/previous-pf-accounts`, formData, { observe: 'response' });
  }

  pfCollectionImport(file: File): Observable<HttpResponse<boolean>> {
    const formData: FormData = new FormData();
    formData.append('file', file);
    return this.http.post<boolean>(this.resourceUrl + `/pf-collections`, formData, { observe: 'response' });
  }

  pfCollectionInterestsImport(file: File): Observable<HttpResponse<boolean>> {
    const formData: FormData = new FormData();
    formData.append('file', file);
    return this.http.post<boolean>(this.resourceUrl + `/pf-collections-interests`, formData, { observe: 'response' });
  }

  previousPfCollectionInterestsImport(file: File): Observable<HttpResponse<boolean>> {
    const formData: FormData = new FormData();
    formData.append('file', file);
    return this.http.post<boolean>(this.resourceUrl + `/previous-pf-collections-interests`, formData, { observe: 'response' });
  }

  pfCollectionMonthlyImport(file: File): Observable<HttpResponse<boolean>> {
    const formData: FormData = new FormData();
    formData.append('file', file);
    return this.http.post<boolean>(this.resourceUrl + `/pf-collections-monthly`, formData, { observe: 'response' });
  }

  previousPfCollectionMonthlyImport(file: File): Observable<HttpResponse<boolean>> {
    const formData: FormData = new FormData();
    formData.append('file', file);
    return this.http.post<boolean>(this.resourceUrl + `/previous-pf-collections-monthly`, formData, { observe: 'response' });
  }

  pfCollectionOpeningBalanceImport(file: File): Observable<HttpResponse<boolean>> {
    const formData: FormData = new FormData();
    formData.append('file', file);
    return this.http.post<boolean>(this.resourceUrl + `/pf-collections-opening-balance`, formData, { observe: 'response' });
  }

  previousPfCollectionOpeningBalanceImport(file: File): Observable<HttpResponse<boolean>> {
    const formData: FormData = new FormData();
    formData.append('file', file);
    return this.http.post<boolean>(this.resourceUrl + `/previous-pf-collections-opening-balance`, formData, { observe: 'response' });
  }

  pfLoanImport(file: File): Observable<HttpResponse<boolean>> {
    const formData: FormData = new FormData();
    formData.append('file', file);
    return this.http.post<boolean>(this.resourceUrl + `/pf-loans`, formData, { observe: 'response' });
  }

  setGrossAndBasicToAllPfCollection(): Observable<HttpResponse<boolean>> {
    return this.http.get<boolean>(this.resourceUrl + `/set-gross-and-basic-to-pf-collection`, { observe: 'response' });
  }

  importGrossAndBasicToPfCollections(file: File): Observable<HttpResponse<boolean>> {
    const formData: FormData = new FormData();
    formData.append('file', file);
    return this.http.post<boolean>(this.resourceUrl + `/import-gross-and-basic-to-pf-collection`, formData, { observe: 'response' });
  }
}
