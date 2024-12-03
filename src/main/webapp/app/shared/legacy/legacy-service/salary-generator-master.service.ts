import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ISalaryGeneratorMaster } from '../legacy-model/salary-generator-master.model';
import { createRequestOption } from '../../../core/request/request-util';
import { ApplicationConfigService } from '../../../core/config/application-config.service';

type EntityResponseType = HttpResponse<ISalaryGeneratorMaster>;
type EntityArrayResponseType = HttpResponse<ISalaryGeneratorMaster[]>;

@Injectable({ providedIn: 'root' })
export class SalaryGeneratorMasterService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/payroll-mgt/salary-generator-masters');
  public fileResourceUrl = this.applicationConfigService.getEndpointFor('/api/payroll-mgt/export/employee-salary/');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(salaryGeneratorMaster: ISalaryGeneratorMaster): Observable<EntityResponseType> {
    return this.http.post<ISalaryGeneratorMaster>(this.resourceUrl, salaryGeneratorMaster, { observe: 'response' });
  }

  update(salaryGeneratorMaster: ISalaryGeneratorMaster): Observable<EntityResponseType> {
    return this.http.put<ISalaryGeneratorMaster>(this.resourceUrl, salaryGeneratorMaster, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ISalaryGeneratorMaster>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ISalaryGeneratorMaster[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  makeSalaryVisibleToEmployee(year: string, month: string): Observable<HttpResponse<boolean>> {
    return this.http.get<boolean>(this.resourceUrl + `/make-salary-visible/${year}/${month}`, { observe: 'response' });
  }

  makeSalaryHiddenFromEmployee(year: string, month: string): Observable<HttpResponse<boolean>> {
    return this.http.get<boolean>(this.resourceUrl + `/make-salary-hidden/${year}/${month}`, { observe: 'response' });
  }

  public exportSalaryXlsx(year: String, month: String, type: String): Observable<Blob> {
    //const options = { responseType: 'blob' }; there is no use of this
    const uri = this.fileResourceUrl + year + '/' + month + '/' + type;
    // this.http refers to HttpClient. Note here that you cannot use the generic get<Blob> as it does not compile: instead you "choose" the appropriate API in this way.
    return this.http.get(uri, { responseType: 'blob' });
  }
}
