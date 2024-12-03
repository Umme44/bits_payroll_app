export const textNormalize = (text: string): string => {
  if (text) {
    return text.charAt(0).toUpperCase() + text.slice(1).toLowerCase();
  }
  return '';
};

export const convertToProperCase = (text: string): string => {
  if (!text) return '';

  const words = text.split(' ');
  let properCase = '';

  words.forEach((value, index) => {
    properCase += value.charAt(0).toUpperCase() + value.slice(1, value.length);
    if (index !== words.length - 1) {
      properCase += ' ';
    }
  });

  return properCase;
};
