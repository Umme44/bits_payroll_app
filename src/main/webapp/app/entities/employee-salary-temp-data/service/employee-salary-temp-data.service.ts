import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IEmployeeSalaryTempData, NewEmployeeSalaryTempData } from '../employee-salary-temp-data.model';

export type PartialUpdateEmployeeSalaryTempData = Partial<IEmployeeSalaryTempData> & Pick<IEmployeeSalaryTempData, 'id'>;

export type EntityResponseType = HttpResponse<IEmployeeSalaryTempData>;
export type EntityArrayResponseType = HttpResponse<IEmployeeSalaryTempData[]>;

@Injectable({ providedIn: 'root' })
export class EmployeeSalaryTempDataService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/employee-salary-temp-data');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(employeeSalaryTempData: NewEmployeeSalaryTempData): Observable<EntityResponseType> {
    return this.http.post<IEmployeeSalaryTempData>(this.resourceUrl, employeeSalaryTempData, { observe: 'response' });
  }

  update(employeeSalaryTempData: IEmployeeSalaryTempData): Observable<EntityResponseType> {
    return this.http.put<IEmployeeSalaryTempData>(
      `${this.resourceUrl}/${this.getEmployeeSalaryTempDataIdentifier(employeeSalaryTempData)}`,
      employeeSalaryTempData,
      { observe: 'response' }
    );
  }

  partialUpdate(employeeSalaryTempData: PartialUpdateEmployeeSalaryTempData): Observable<EntityResponseType> {
    return this.http.patch<IEmployeeSalaryTempData>(
      `${this.resourceUrl}/${this.getEmployeeSalaryTempDataIdentifier(employeeSalaryTempData)}`,
      employeeSalaryTempData,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IEmployeeSalaryTempData>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IEmployeeSalaryTempData[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getEmployeeSalaryTempDataIdentifier(employeeSalaryTempData: Pick<IEmployeeSalaryTempData, 'id'>): number {
    return employeeSalaryTempData.id;
  }

  compareEmployeeSalaryTempData(o1: Pick<IEmployeeSalaryTempData, 'id'> | null, o2: Pick<IEmployeeSalaryTempData, 'id'> | null): boolean {
    return o1 && o2 ? this.getEmployeeSalaryTempDataIdentifier(o1) === this.getEmployeeSalaryTempDataIdentifier(o2) : o1 === o2;
  }

  addEmployeeSalaryTempDataToCollectionIfMissing<Type extends Pick<IEmployeeSalaryTempData, 'id'>>(
    employeeSalaryTempDataCollection: Type[],
    ...employeeSalaryTempDataToCheck: (Type | null | undefined)[]
  ): Type[] {
    const employeeSalaryTempData: Type[] = employeeSalaryTempDataToCheck.filter(isPresent);
    if (employeeSalaryTempData.length > 0) {
      const employeeSalaryTempDataCollectionIdentifiers = employeeSalaryTempDataCollection.map(
        employeeSalaryTempDataItem => this.getEmployeeSalaryTempDataIdentifier(employeeSalaryTempDataItem)!
      );
      const employeeSalaryTempDataToAdd = employeeSalaryTempData.filter(employeeSalaryTempDataItem => {
        const employeeSalaryTempDataIdentifier = this.getEmployeeSalaryTempDataIdentifier(employeeSalaryTempDataItem);
        if (employeeSalaryTempDataCollectionIdentifiers.includes(employeeSalaryTempDataIdentifier)) {
          return false;
        }
        employeeSalaryTempDataCollectionIdentifiers.push(employeeSalaryTempDataIdentifier);
        return true;
      });
      return [...employeeSalaryTempDataToAdd, ...employeeSalaryTempDataCollection];
    }
    return employeeSalaryTempDataCollection;
  }
}
