export interface IProcReq {
  id: number;
  quantity?: number | null;
  referenceFilePath?: string | null;

  // itemInformation?: Pick<IItemInformation, 'id' | 'name'> | null;
  // procReqMaster?: Pick<IProcReqMaster, 'id' | 'requisitionNo'> | null;

  referenceFileDataContentType?: string;
  referenceFileData?: any;
  itemInformationName?: string;
  itemInformationCode?: string;
  itemInformationSpecification?: any;
  itemInformationId?: number;
  unitOfMeasurementName?: string;
  procReqMasterRequisitionNo?: string;
  procReqMasterId?: number;
}

export type NewProcReq = Omit<IProcReq, 'id'> & { id: null };
