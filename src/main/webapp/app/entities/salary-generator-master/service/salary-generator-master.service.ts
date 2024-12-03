import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ISalaryGeneratorMaster, NewSalaryGeneratorMaster } from '../salary-generator-master.model';

export type PartialUpdateSalaryGeneratorMaster = Partial<ISalaryGeneratorMaster> & Pick<ISalaryGeneratorMaster, 'id'>;

export type EntityResponseType = HttpResponse<ISalaryGeneratorMaster>;
export type EntityArrayResponseType = HttpResponse<ISalaryGeneratorMaster[]>;

@Injectable({ providedIn: 'root' })
export class SalaryGeneratorMasterService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/payroll-mgt/salary-generator-masters');
  protected fileResourceUrl = this.applicationConfigService.getEndpointFor('/api/payroll-mgt/export/employee-salary/');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(salaryGeneratorMaster: NewSalaryGeneratorMaster): Observable<EntityResponseType> {
    return this.http.post<ISalaryGeneratorMaster>(this.resourceUrl, salaryGeneratorMaster, { observe: 'response' });
  }

  update(salaryGeneratorMaster: ISalaryGeneratorMaster): Observable<EntityResponseType> {
    return this.http.put<ISalaryGeneratorMaster>(
      `${this.resourceUrl}/${this.getSalaryGeneratorMasterIdentifier(salaryGeneratorMaster)}`,
      salaryGeneratorMaster,
      { observe: 'response' }
    );
  }

  partialUpdate(salaryGeneratorMaster: PartialUpdateSalaryGeneratorMaster): Observable<EntityResponseType> {
    return this.http.patch<ISalaryGeneratorMaster>(
      `${this.resourceUrl}/${this.getSalaryGeneratorMasterIdentifier(salaryGeneratorMaster)}`,
      salaryGeneratorMaster,
      { observe: 'response' }
    );
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

  getSalaryGeneratorMasterIdentifier(salaryGeneratorMaster: Pick<ISalaryGeneratorMaster, 'id'>): number {
    return salaryGeneratorMaster.id;
  }

  compareSalaryGeneratorMaster(o1: Pick<ISalaryGeneratorMaster, 'id'> | null, o2: Pick<ISalaryGeneratorMaster, 'id'> | null): boolean {
    return o1 && o2 ? this.getSalaryGeneratorMasterIdentifier(o1) === this.getSalaryGeneratorMasterIdentifier(o2) : o1 === o2;
  }

  addSalaryGeneratorMasterToCollectionIfMissing<Type extends Pick<ISalaryGeneratorMaster, 'id'>>(
    salaryGeneratorMasterCollection: Type[],
    ...salaryGeneratorMastersToCheck: (Type | null | undefined)[]
  ): Type[] {
    const salaryGeneratorMasters: Type[] = salaryGeneratorMastersToCheck.filter(isPresent);
    if (salaryGeneratorMasters.length > 0) {
      const salaryGeneratorMasterCollectionIdentifiers = salaryGeneratorMasterCollection.map(
        salaryGeneratorMasterItem => this.getSalaryGeneratorMasterIdentifier(salaryGeneratorMasterItem)!
      );
      const salaryGeneratorMastersToAdd = salaryGeneratorMasters.filter(salaryGeneratorMasterItem => {
        const salaryGeneratorMasterIdentifier = this.getSalaryGeneratorMasterIdentifier(salaryGeneratorMasterItem);
        if (salaryGeneratorMasterCollectionIdentifiers.includes(salaryGeneratorMasterIdentifier)) {
          return false;
        }
        salaryGeneratorMasterCollectionIdentifiers.push(salaryGeneratorMasterIdentifier);
        return true;
      });
      return [...salaryGeneratorMastersToAdd, ...salaryGeneratorMasterCollection];
    }
    return salaryGeneratorMasterCollection;
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
